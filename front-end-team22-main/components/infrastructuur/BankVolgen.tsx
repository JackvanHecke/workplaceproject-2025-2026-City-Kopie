"use client";

import LocationService from "@services/LocationService";
import React, { useEffect, useState } from "react";

type Props = { profileId: number };

type FollowEntry = {
  id: string;
  benchName: string;
  radiusKm: number;
  followedAt: string;
};

function normalizeName(name: string) {
  return name.trim().replace(/\s+/g, " ").toLowerCase();
}

export default function BankVolgen({ profileId }: Props) {
  const [benchName, setBenchName] = useState("");
  const [radiusKm, setRadiusKm] = useState<number>(10);

  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [message, setMessage] = useState<string | null>(null);

  const [followed, setFollowed] = useState<FollowEntry[]>([]);

  const [benchNames, setBenchNames] = useState<string[]>([]);

  useEffect(() => {
    let cancelled = false;

    async function loadNames() {
      try {
        const data = await LocationService.getAllNames();
        const names = data.sort();
        if (!cancelled) setBenchNames(names);
      } catch (e) {
        console.error("Failed to load bench names", e);
        if (!cancelled) setBenchNames([]);
      }
    }

    loadNames();
    return () => {
      cancelled = true;
    };
  }, []);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!benchName.trim()) return;

    setSaving(true);
    setError(null);
    setMessage(null);

    try {
      const normalized = normalizeName(benchName);

      const existing = followed.find(
        (f) => normalizeName(f.benchName) === normalized
      );

      if (existing) {
        await new Promise((r) => setTimeout(r, 150));

        setFollowed((prev) =>
          prev.map((f) =>
            f.id === existing.id
              ? { ...f, radiusKm, followedAt: new Date().toISOString() }
              : f
          )
        );

        setMessage(
          `Je volgde "${existing.benchName}" al — radius aangepast naar ${radiusKm} km.`
        );
        setBenchName("");
        return;
      }

      await new Promise((r) => setTimeout(r, 150));

      const entry: FollowEntry = {
        id: crypto.randomUUID(),
        benchName: benchName.trim(),
        radiusKm,
        followedAt: new Date().toISOString(),
      };

      setFollowed((prev) => [entry, ...prev]);
      setMessage(`Je volgt nu "${entry.benchName}" (radius: ${radiusKm} km).`);
      setBenchName("");
    } catch (err) {
      console.error(err);
      setError("Kon bank niet laten volgen.");
    } finally {
      setSaving(false);
    }
  }

  function handleDelete(id: string) {
    setFollowed((prev) => prev.filter((f) => f.id !== id));
    setMessage("Bank niet meer gevolgd.");
    setError(null);
  }

  return (
    <div className="mt-6">
      <form
        onSubmit={handleSubmit}
        className="rounded-2xl bg-white shadow p-4 flex flex-col sm:flex-row gap-4 sm:items-end"
      >
        <div className="flex flex-col">
          <label className="text-sm text-gray-600">Bench naam</label>

          <input
            type="text"
            className="mt-1 rounded border px-2 py-1"
            value={benchName}
            onChange={(e) => {
              setBenchName(e.target.value);
              setError(null);
              setMessage(null);
            }}
            placeholder="bv. Station Bench Leuven"
            list="bench-suggestions"
            required
          />

          <datalist id="bench-suggestions">
            {benchNames.map((n) => (
              <option key={n} value={n} />
            ))}
          </datalist>
        </div>

        <div className="flex flex-col">
          <label className="text-sm text-gray-600">Radius (km)</label>
          <select
            className="mt-1 rounded border px-2 py-1"
            value={radiusKm}
            onChange={(e) => setRadiusKm(Number(e.target.value))}
          >
            <option value={5}>5 km</option>
            <option value={10}>10 km</option>
            <option value={15}>15 km</option>
            <option value={25}>25 km</option>
          </select>
        </div>

        <button
          type="submit"
          disabled={saving || !benchName.trim()}
          className="inline-flex items-center justify-center rounded-lg bg-black px-4 py-2 text-white disabled:opacity-50"
        >
          {saving ? "Opslaan..." : "Bank volgen"}
        </button>

        <div className="flex-1 text-sm">
          {error && <p className="text-red-600">{error}</p>}
          {message && !error && <p className="text-green-600">{message}</p>}
        </div>
      </form>

      {followed.length > 0 && (
        <div className="mt-4 rounded-2xl bg-white shadow p-4">
          <h3 className="font-semibold mb-2">Gevolgde banken (lokaal)</h3>

          <ul className="space-y-2 text-sm">
            {followed.map((f) => (
              <li
                key={f.id}
                className="rounded border p-3 flex items-start justify-between gap-3"
              >
                <div>
                  <div className="font-medium">{f.benchName}</div>
                  <div className="text-gray-600">Radius: {f.radiusKm} km</div>
                </div>

                <button
                  type="button"
                  onClick={() => handleDelete(f.id)}
                  className="rounded-lg border px-3 py-1 text-sm hover:bg-gray-50"
                  aria-label={`Verwijder ${f.benchName}`}
                >
                  Verwijder
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
