"use client";

import React, { useEffect, useMemo, useState } from "react";
import CoachService from "../../services/CoachService";
import { Coach } from "../../types";

type PaidFilter = "all" | "free" | "paid";
type AvailabilityFilter = "all" | "now" | "range";
type RadiusKm = 0 | 1 | 5 | 10 | 20 | 50;
type ExperienceFilter = "all" | "starter" | "experienced" | "expert";

const FAVORITES_KEY = "favoriteCoaches:v2";

function safeJsonParse<T>(raw: string | null, fallback: T): T {
  try {
    if (!raw) return fallback;
    return JSON.parse(raw) as T;
  } catch {
    return fallback;
  }
}

function normalize(s: string) {
  return (s ?? "").toLowerCase().trim();
}

function getCoachIsFree(coach: Coach): boolean {
  if (coach.isFree === true) return true;
  const firstOffer = coach.offers?.[0];
  return firstOffer?.freeOrPaid === "free";
}

function getCoachTargetGroups(coach: Coach): string[] {
  const groups = (coach.offers ?? [])
    .map((o) => (o.targetGroup ?? "").trim())
    .filter(Boolean);
  return Array.from(new Set(groups));
}

function getCoachCities(coach: Coach): string[] {
  const cities = (coach.locations ?? [])
    .map((l: any) => (l?.benchCity ?? l?.city ?? "").trim())
    .filter(Boolean);
  return Array.from(new Set(cities));
}

function fmtDateTime(iso?: string) {
  if (!iso) return "-";
  try {
    return new Date(iso).toLocaleString();
  } catch {
    return iso;
  }
}

function overlaps(aStart: Date, aEnd: Date, bStart: Date, bEnd: Date) {
  return aStart <= bEnd && bStart <= aEnd;
}

function isCoachAvailableNow(coach: Coach, now: Date) {
  const slots = coach.availability ?? [];
  for (const s of slots) {
    if (!s?.availableFrom || !s?.availableTo) continue;
    const from = new Date(s.availableFrom);
    const to = new Date(s.availableTo);
    if (from <= now && now <= to) return true;

    if (s.isRecurring) return true;
  }
  return false;
}

function isCoachAvailableInRange(
  coach: Coach,
  rangeStart: Date,
  rangeEnd: Date
) {
  const slots = coach.availability ?? [];
  for (const s of slots) {
    if (!s?.availableFrom || !s?.availableTo) continue;
    const from = new Date(s.availableFrom);
    const to = new Date(s.availableTo);

    if (overlaps(from, to, rangeStart, rangeEnd)) return true;
    if (s.isRecurring) return true;
  }
  return false;
}

function haversineKm(aLat: number, aLng: number, bLat: number, bLng: number) {
  const R = 6371;
  const dLat = ((bLat - aLat) * Math.PI) / 180;
  const dLng = ((bLng - aLng) * Math.PI) / 180;
  const lat1 = (aLat * Math.PI) / 180;
  const lat2 = (bLat * Math.PI) / 180;

  const x =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);

  const c = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
  return R * c;
}

function minDistanceKmToCoachLocations(
  coach: Coach,
  origin: { lat: number; lng: number }
) {
  const locs = coach.locations ?? [];
  let best = Infinity;

  for (const loc of locs) {
    const lat = (loc as any)?.latitude;
    const lng = (loc as any)?.longitude;
    if (typeof lat === "number" && typeof lng === "number") {
      const d = haversineKm(origin.lat, origin.lng, lat, lng);
      if (d < best) best = d;
    }
  }
  return best;
}

// Best-effort experience detection:
// 1) Look for "x jaar" / "x years" in bio
// 2) number of offers + availability slots (activity proxy)
function estimateExperience(coach: Coach): {
  label: ExperienceFilter;
  score: number;
} {
  const bio = normalize(coach.bio ?? "");
  const m = bio.match(/(\d{1,2})\s*(jaar|jaren|years)/i);
  const years = m ? Number(m[1]) : null;

  if (years !== null && !Number.isNaN(years)) {
    if (years >= 10) return { label: "expert", score: 100 + years };
    if (years >= 4) return { label: "experienced", score: 60 + years };
    return { label: "starter", score: 20 + years };
  }

  const offers = coach.offers?.length ?? 0;
  const avail = coach.availability?.length ?? 0;
  const score = offers * 10 + avail * 5;

  if (score >= 60) return { label: "expert", score };
  if (score >= 25) return { label: "experienced", score };
  if (score > 0) return { label: "starter", score };
  return { label: "all", score: 0 };
}

function Chip({ children }: { children: React.ReactNode }) {
  return (
    <span className="px-2 py-1 rounded bg-gray-100 text-xs">{children}</span>
  );
}

function CoachCard({
  coach,
  isFavorite,
  onToggleFavorite,
  variant,
}: {
  coach: Coach;
  isFavorite: boolean;
  onToggleFavorite: () => void;
  variant?: "favorite" | "normal";
}) {
  const id = coach.id ?? -1;
  const cities = getCoachCities(coach);
  const groups = getCoachTargetGroups(coach);
  const isFree = getCoachIsFree(coach);
  const exp = estimateExperience(coach);

  const firstOffer = coach.offers?.[0];
  const firstAvailability = coach.availability?.[0];

  return (
    <div
      className={`rounded border bg-white p-4 shadow-sm ${
        variant === "favorite" ? "ring-2 ring-yellow-200" : ""
      }`}
      data-cy={`coach-card-${id}`}
    >
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0">
          <h3
            className="text-lg font-semibold truncate"
            data-cy={`coach-card-name-${id}`}
          >
            {coach.name ?? "Unknown trainer"}
          </h3>
          <div className="text-sm text-gray-600 truncate">
            {cities.length > 0 ? cities.join(", ") : "No city info"}
          </div>
        </div>

        <button
          onClick={onToggleFavorite}
          className="px-3 py-1 rounded border bg-white hover:bg-gray-50 text-sm whitespace-nowrap"
          data-cy={`coach-fav-${id}`}
          title={isFavorite ? "Remove favorite" : "Add favorite"}
        >
          {isFavorite ? "★ Favoriet" : "☆ Favoriet"}
        </button>
      </div>

      <div className="mt-3 flex flex-wrap gap-2">
        <Chip>{isFree ? "Gratis" : "Betalend"}</Chip>
        {exp.label !== "all" && <Chip>Ervaring: {exp.label}</Chip>}
        {groups.slice(0, 4).map((g) => (
          <Chip key={g}>{g}</Chip>
        ))}
        {groups.length > 4 && <Chip>+{groups.length - 4} meer</Chip>}
      </div>

      {coach.bio && (
        <p
          className="mt-3 text-sm text-gray-700 whitespace-pre-wrap line-clamp-4"
          data-cy={`coach-card-bio-${id}`}
        >
          {coach.bio}
        </p>
      )}

      <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-3 text-sm">
        <div className="rounded bg-gray-50 p-3">
          <div className="font-medium mb-1">Aanbod</div>
          <div className="text-gray-700">
            {firstOffer ? (
              <>
                <div className="truncate">{firstOffer.offerType ?? "—"}</div>
                <div className="text-xs text-gray-500">
                  {firstOffer.targetGroup
                    ? `Doelgroep: ${firstOffer.targetGroup}`
                    : " "}
                </div>
              </>
            ) : (
              <div className="text-gray-500">Geen aanbod info</div>
            )}
          </div>
        </div>

        <div className="rounded bg-gray-50 p-3">
          <div className="font-medium mb-1">Beschikbaarheid</div>
          <div className="text-gray-700">
            {firstAvailability ? (
              <>
                <div className="text-xs text-gray-500">
                  {firstAvailability.isRecurring ? "Terugkerend" : "Eenmalig"}
                </div>
                <div className="truncate">
                  {fmtDateTime(firstAvailability.availableFrom)} →{" "}
                  {fmtDateTime(firstAvailability.availableTo)}
                </div>
              </>
            ) : (
              <div className="text-gray-500">Geen beschikbaarheid info</div>
            )}
          </div>
        </div>
      </div>

      <div className="mt-4 flex gap-2 flex-wrap">
        <button
          className="px-3 py-2 rounded border bg-white hover:bg-gray-50 text-sm"
          onClick={() =>
            alert(
              "Contact modal next: needs backend endpoint (POST /coaches/{id}/contact) or a mocked version."
            )
          }
          data-cy={`coach-contact-${id}`}
        >
          Contacteer
        </button>
      </div>
    </div>
  );
}

export default function CoachDirectory() {
  const [coaches, setCoaches] = useState<Coach[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [q, setQ] = useState("");
  const [paidFilter, setPaidFilter] = useState<PaidFilter>("all");
  const [cityFilter, setCityFilter] = useState<string>("all");
  const [targetGroupFilter, setTargetGroupFilter] = useState<string>("all");
  const [radiusKm, setRadiusKm] = useState<RadiusKm>(0);

  const [availabilityFilter, setAvailabilityFilter] =
    useState<AvailabilityFilter>("all");
  const [rangeStart, setRangeStart] = useState<string>("");
  const [rangeEnd, setRangeEnd] = useState<string>("");

  const [experienceFilter, setExperienceFilter] =
    useState<ExperienceFilter>("all");

  const [favoriteIds, setFavoriteIds] = useState<number[]>([]);

  useEffect(() => {
    const fav = safeJsonParse<number[]>(
      typeof window !== "undefined"
        ? localStorage.getItem(FAVORITES_KEY)
        : null,
      []
    );
    setFavoriteIds(Array.isArray(fav) ? fav : []);
  }, []);

  const persistFavorites = (ids: number[]) => {
    setFavoriteIds(ids);
    try {
      localStorage.setItem(FAVORITES_KEY, JSON.stringify(ids));
    } catch {}
  };

  const toggleFavorite = (coachId?: number) => {
    if (!coachId) return;
    const isFav = favoriteIds.includes(coachId);
    persistFavorites(
      isFav
        ? favoriteIds.filter((x) => x !== coachId)
        : [coachId, ...favoriteIds]
    );
  };

  useEffect(() => {
    (async () => {
      setLoading(true);
      setError(null);

      try {
        const res = await CoachService.getAll();
        if (!res.ok) {
          const txt = await res.text();
          throw new Error(txt || "Failed to load coaches");
        }
        const data: Coach[] = await res.json();
        setCoaches(Array.isArray(data) ? data : []);
      } catch (e: any) {
        setError(e?.message ?? "Failed to load coaches");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const cityOptions = useMemo(() => {
    const allCities = coaches.flatMap(getCoachCities);
    return [
      "all",
      ...Array.from(new Set(allCities)).sort((a, b) => a.localeCompare(b)),
    ];
  }, [coaches]);

  const targetGroupOptions = useMemo(() => {
    const groups = coaches.flatMap(getCoachTargetGroups);
    return [
      "all",
      ...Array.from(new Set(groups)).sort((a, b) => a.localeCompare(b)),
    ];
  }, [coaches]);

  const radiusOrigin = useMemo(() => {
    if (radiusKm === 0) return null;
    if (cityFilter === "all") return null;

    for (const c of coaches) {
      for (const loc of c.locations ?? []) {
        const city = (
          (loc as any)?.benchCity ??
          (loc as any)?.city ??
          ""
        ).trim();
        if (city !== cityFilter) continue;
        const lat = (loc as any)?.latitude;
        const lng = (loc as any)?.longitude;
        if (typeof lat === "number" && typeof lng === "number") {
          return { lat, lng };
        }
      }
    }
    return null;
  }, [radiusKm, cityFilter, coaches]);

  const favorites = useMemo(() => {
    const favSet = new Set(favoriteIds);
    return coaches.filter((c) => c.id && favSet.has(c.id));
  }, [coaches, favoriteIds]);

  const results = useMemo(() => {
    const nq = normalize(q);
    const now = new Date();

    const parsedStart = rangeStart ? new Date(rangeStart) : null;
    const parsedEnd = rangeEnd ? new Date(rangeEnd) : null;

    const matchesSearch = (coach: Coach) => {
      if (!nq) return true;

      const name = coach.name ?? "";
      const bio = coach.bio ?? "";
      const cities = getCoachCities(coach).join(" ");
      const groups = getCoachTargetGroups(coach).join(" ");
      const offerText = (coach.offers ?? [])
        .map((o) => `${o.offerType ?? ""} ${o.description ?? ""}`)
        .join(" ");

      const hay = normalize(`${name} ${bio} ${cities} ${groups} ${offerText}`);
      return hay.includes(nq);
    };

    const matchesPaid = (coach: Coach) => {
      if (paidFilter === "all") return true;
      const isFree = getCoachIsFree(coach);
      return paidFilter === "free" ? isFree : !isFree;
    };

    const matchesCity = (coach: Coach) => {
      if (cityFilter === "all") return true;
      return getCoachCities(coach).includes(cityFilter);
    };

    const matchesTargetGroup = (coach: Coach) => {
      if (targetGroupFilter === "all") return true;
      return getCoachTargetGroups(coach).includes(targetGroupFilter);
    };

    const matchesRadius = (coach: Coach) => {
      if (radiusKm === 0) return true;
      if (!radiusOrigin) return true;
      const d = minDistanceKmToCoachLocations(coach, radiusOrigin);
      return Number.isFinite(d) ? d <= radiusKm : true;
    };

    const matchesAvailability = (coach: Coach) => {
      if (availabilityFilter === "all") return true;
      if (availabilityFilter === "now") return isCoachAvailableNow(coach, now);

      if (!parsedStart || !parsedEnd) return true;
      return isCoachAvailableInRange(coach, parsedStart, parsedEnd);
    };

    const matchesExperience = (coach: Coach) => {
      if (experienceFilter === "all") return true;
      const exp = estimateExperience(coach).label;
      if (experienceFilter === "starter")
        return exp === "starter" || exp === "experienced" || exp === "expert";
      if (experienceFilter === "experienced")
        return exp === "experienced" || exp === "expert";
      if (experienceFilter === "expert") return exp === "expert";
      return true;
    };

    const favSet = new Set(favoriteIds);

    return coaches
      .filter((c) => !(c.id && favSet.has(c.id)))
      .filter(matchesSearch)
      .filter(matchesPaid)
      .filter(matchesCity)
      .filter(matchesTargetGroup)
      .filter(matchesRadius)
      .filter(matchesAvailability)
      .filter(matchesExperience)
      .sort((a, b) => (a.name ?? "").localeCompare(b.name ?? ""));
  }, [
    coaches,
    favoriteIds,
    q,
    paidFilter,
    cityFilter,
    targetGroupFilter,
    radiusKm,
    radiusOrigin,
    availabilityFilter,
    rangeStart,
    rangeEnd,
    experienceFilter,
  ]);

  return (
    <section className="max-w-6xl mx-auto" data-cy="coach-directory">
      <div className="flex flex-col gap-3 mb-6">
        <div className="flex items-end justify-between gap-4 flex-wrap">
          <div>
            <h1 className="text-2xl font-semibold">Trainers & Database</h1>
            <p className="text-sm text-gray-600">
              Zoek trainers op <strong>locatie</strong>,{" "}
              <strong>beschikbaarheid</strong>, <strong>doelgroepen</strong> en{" "}
              <strong>ervaring</strong>. Favorieten staan bovenaan in een aparte
              lijst.
            </p>
          </div>

          <button
            onClick={() => window.location.reload()}
            className="px-4 py-2 rounded border bg-white hover:bg-gray-50"
            data-cy="coach-directory-refresh"
          >
            Refresh
          </button>
        </div>

        <div className="rounded border bg-white p-4">
          <div className="grid grid-cols-1 md:grid-cols-6 gap-3">
            <input
              className="md:col-span-2 rounded border px-3 py-2"
              placeholder="Zoek op naam, stad, doelgroep, aanbod…"
              value={q}
              onChange={(e) => setQ(e.target.value)}
              data-cy="coach-search"
            />

            <select
              className="rounded border px-3 py-2"
              value={cityFilter}
              onChange={(e) => setCityFilter(e.target.value)}
              data-cy="coach-filter-city"
            >
              {cityOptions.map((c) => (
                <option key={c} value={c}>
                  {c === "all" ? "Alle steden" : c}
                </option>
              ))}
            </select>

            <select
              className="rounded border px-3 py-2"
              value={targetGroupFilter}
              onChange={(e) => setTargetGroupFilter(e.target.value)}
              data-cy="coach-filter-targetgroup"
            >
              {targetGroupOptions.map((g) => (
                <option key={g} value={g}>
                  {g === "all" ? "Alle doelgroepen" : g}
                </option>
              ))}
            </select>

            <select
              className="rounded border px-3 py-2"
              value={availabilityFilter}
              onChange={(e) =>
                setAvailabilityFilter(e.target.value as AvailabilityFilter)
              }
              data-cy="coach-filter-availability"
            >
              <option value="all">Alle beschikbaarheden</option>
              <option value="now">Beschikbaar nu</option>
              <option value="range">Beschikbaar in periode</option>
            </select>

            <select
              className="rounded border px-3 py-2"
              value={experienceFilter}
              onChange={(e) =>
                setExperienceFilter(e.target.value as ExperienceFilter)
              }
              data-cy="coach-filter-experience"
              title="Best-effort op basis van bio/aanbod"
            >
              <option value="all">Alle ervaring</option>
              <option value="starter">Starter+</option>
              <option value="experienced">Ervaren+</option>
              <option value="expert">Expert</option>
            </select>
          </div>

          <div className="mt-3 grid grid-cols-1 md:grid-cols-6 gap-3 items-end">
            <select
              className="rounded border px-3 py-2"
              value={paidFilter}
              onChange={(e) => setPaidFilter(e.target.value as PaidFilter)}
              data-cy="coach-filter-paid"
            >
              <option value="all">Gratis + Betalend</option>
              <option value="free">Enkel gratis</option>
              <option value="paid">Enkel betalend</option>
            </select>

            <div className="md:col-span-2">
              <label className="block text-xs text-gray-600 mb-1">
                Radius (lager prioriteit)
              </label>
              <select
                className="w-full rounded border px-3 py-2"
                value={radiusKm}
                onChange={(e) =>
                  setRadiusKm(Number(e.target.value) as RadiusKm)
                }
                data-cy="coach-filter-radius"
                title="Werkt het best als benches latitude/longitude hebben"
              >
                <option value={0}>Geen radius</option>
                <option value={1}>1 km</option>
                <option value={5}>5 km</option>
                <option value={10}>10 km</option>
                <option value={20}>20 km</option>
                <option value={50}>50 km</option>
              </select>
              {radiusKm !== 0 && cityFilter !== "all" && !radiusOrigin && (
                <div className="mt-1 text-xs text-amber-700">
                  Geen lat/lng gevonden voor “{cityFilter}”. Radius wordt niet
                  toegepast.
                </div>
              )}
            </div>

            {availabilityFilter === "range" && (
              <>
                <div className="md:col-span-1">
                  <label className="block text-xs text-gray-600 mb-1">
                    Van
                  </label>
                  <input
                    type="datetime-local"
                    className="w-full rounded border px-3 py-2"
                    value={rangeStart}
                    onChange={(e) => setRangeStart(e.target.value)}
                    data-cy="coach-filter-range-start"
                  />
                </div>
                <div className="md:col-span-1">
                  <label className="block text-xs text-gray-600 mb-1">
                    Tot
                  </label>
                  <input
                    type="datetime-local"
                    className="w-full rounded border px-3 py-2"
                    value={rangeEnd}
                    onChange={(e) => setRangeEnd(e.target.value)}
                    data-cy="coach-filter-range-end"
                  />
                </div>
              </>
            )}
          </div>
        </div>
      </div>

      {loading && <div className="py-12 text-center">Loading trainers…</div>}
      {error && <div className="py-4 text-red-600">Error: {error}</div>}

      {!loading && !error && (
        <>
          <div className="mb-6">
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-xl font-semibold">Jouw favorieten</h2>
              <div className="text-sm text-gray-600">
                {favorites.length > 0
                  ? `${favorites.length} favoriet(en)`
                  : "Nog geen favorieten"}
              </div>
            </div>

            {favorites.length === 0 ? (
              <div className="rounded border bg-white p-4 text-sm text-gray-600">
                Voeg trainers toe aan favorieten met de ⭐ knop. Ze verschijnen
                hier bovenaan.
              </div>
            ) : (
              <div
                className="grid grid-cols-1 md:grid-cols-2 gap-4"
                data-cy="coach-favorites"
              >
                {favorites
                  .slice()
                  .sort((a, b) => (a.name ?? "").localeCompare(b.name ?? ""))
                  .map((coach) => (
                    <CoachCard
                      key={coach.id}
                      coach={coach}
                      isFavorite={true}
                      onToggleFavorite={() => toggleFavorite(coach.id)}
                      variant="favorite"
                    />
                  ))}
              </div>
            )}
          </div>

          <div className="flex items-center justify-between mb-3">
            <h2 className="text-xl font-semibold">Alle trainers</h2>
            <div
              className="text-sm text-gray-600"
              data-cy="coach-results-count"
            >
              {results.length} trainer(s) gevonden
            </div>
          </div>

          {results.length === 0 ? (
            <div
              className="rounded border bg-white p-6 text-center text-gray-600"
              data-cy="coach-no-results"
            >
              Geen trainers gevonden met deze filters.
            </div>
          ) : (
            <div
              className="grid grid-cols-1 md:grid-cols-2 gap-4"
              data-cy="coach-cards"
            >
              {results.map((coach) => (
                <CoachCard
                  key={coach.id ?? Math.random()}
                  coach={coach}
                  isFavorite={coach.id ? favoriteIds.includes(coach.id) : false}
                  onToggleFavorite={() => toggleFavorite(coach.id)}
                />
              ))}
            </div>
          )}
        </>
      )}
    </section>
  );
}
