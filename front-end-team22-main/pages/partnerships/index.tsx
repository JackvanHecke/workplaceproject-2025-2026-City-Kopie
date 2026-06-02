import Head from "next/head";
import Header from "@components/header";
import SideNav from "@components/sideNav";

import React, { useEffect, useMemo, useState } from "react";
import StakeholderList from "@components/stakeholders/StakeholderList";
import StakeholderForm from "@components/stakeholders/StakeholderForm";
import StakeholderService from "@services/StakeholderService";
import LocationService from "@services/LocationService";
import exportToCsv from "utils/exportToCsv";

import PartnershipsMap from "@components/stakeholders/PartnershipMap";
import type {
  Location,
  PartnershipCategory,
  LocationPartnershipDTO,
  Stakeholder,
} from "@types";
import { useUser } from "utils/UserContext";
import { useRouter } from "next/navigation";

import PartnershipCategoryService from "@services/PartnershipCategoryService";
import LocationPartnershipService from "@services/LocationPartnershipService";

const StakeholdersPage: React.FC = () => {
  const [benches, setBenches] = useState<Location[]>([]);
  const [selectedBenchId, setSelectedBenchId] = useState<number | null>(null);

  const [categories, setCategories] = useState<PartnershipCategory[]>([]);
  const [decidedCategoryIds, setDecidedCategoryIds] = useState<Set<number>>(
    new Set()
  );

  const [stakeholders, setStakeholders] = useState<Stakeholder[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<Stakeholder | null>(null);

  const router = useRouter();
  const { currentUser, loading: userLoading } = useUser();

  const roleUpper = (currentUser?.role || "").toUpperCase();
  const isAllowed = roleUpper === "ADMIN" || roleUpper === "CLIENT";

  const ownerKey = useMemo(() => {
    if (!currentUser) return "";
    return currentUser.email || currentUser.name || "";
  }, [currentUser]);

  const loadBenches = async () => {
    if (!currentUser) {
      setBenches([]);
      setSelectedBenchId(null);
      return;
    }

    if (roleUpper === "CLIENT") {
      if (!ownerKey) {
        setBenches([]);
        setSelectedBenchId(null);
        setError("Client profile has no email/name to match benchOwner.");
        return;
      }
      const mine = await LocationService.getMine(ownerKey);
      setBenches(mine);
      setSelectedBenchId((mine[0]?.id as number) ?? null);
      return;
    }

    const all = await LocationService.getAll();
    setBenches(all);
    setSelectedBenchId((all[0]?.id as number) ?? null);
  };

  const loadCategories = async () => {
    const list = await PartnershipCategoryService.getAll();
    setCategories(list);
  };

  const loadBenchScopedData = async (benchId: number) => {
    const [stakeholderList, partnershipList] = await Promise.all([
      StakeholderService.getForLocation(benchId),
      LocationPartnershipService.getForLocation(benchId),
    ]);

    setStakeholders(stakeholderList);

    const decided = new Set<number>(
      (partnershipList ?? [])
        .filter((p: LocationPartnershipDTO) => p.decided)
        .map((p: LocationPartnershipDTO) => p.categoryId)
    );
    setDecidedCategoryIds(decided);
  };

  const refreshSelectedBench = async () => {
    if (!selectedBenchId) {
      setStakeholders([]);
      setDecidedCategoryIds(new Set<number>());
      return;
    }
    await loadBenchScopedData(selectedBenchId);
  };

  const handleToggleCategory = async (categoryId: number) => {
    if (!selectedBenchId) return;

    // optimistic update
    setDecidedCategoryIds((prev) => {
      const next = new Set(prev);
      if (next.has(categoryId)) next.delete(categoryId);
      else next.add(categoryId);
      return next;
    });

    try {
      const dto = await LocationPartnershipService.toggle(
        selectedBenchId,
        categoryId
      );

      setDecidedCategoryIds((prev) => {
        const next = new Set(prev);
        if (dto.decided) next.add(dto.categoryId);
        else next.delete(dto.categoryId);
        return next;
      });
    } catch (err: any) {
      // refetch truth
      try {
        const list = await LocationPartnershipService.getForLocation(
          selectedBenchId
        );
        setDecidedCategoryIds(
          new Set<number>(
            list.filter((x) => x.decided).map((x) => x.categoryId)
          )
        );
      } catch {
        // ignore
      }
      alert("Error updating partnership: " + (err.message ?? err));
    }
  };

  const handleSave = async (s: Stakeholder) => {
    try {
      if (s.id) await StakeholderService.update(s.id, s);
      else await StakeholderService.create(s);

      await refreshSelectedBench();
      setShowForm(false);
      setEditing(null);
    } catch (err: any) {
      alert("Error saving stakeholder: " + (err.message ?? err));
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this stakeholder?")) return;

    try {
      await StakeholderService.delete(id);
      await refreshSelectedBench();
    } catch (err: any) {
      alert("Error deleting stakeholder: " + (err.message ?? err));
    }
  };

  const handleExport = () => exportToCsv(stakeholders, "stakeholders.csv");

  // auth gate + initial load
  useEffect(() => {
    if (userLoading) return;

    if (!currentUser) {
      router.push("/login");
      return;
    }
    if (roleUpper === "END_USER" || roleUpper === "COACH") {
      router.push("/");
      return;
    }

    (async () => {
      try {
        setLoading(true);
        setError(null);
        await Promise.all([loadCategories(), loadBenches()]);
      } catch (err: any) {
        setError(err.message ?? "Failed to load page data.");
      } finally {
        setLoading(false);
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentUser, userLoading, router]);

  // whenever bench changes, load bench-scoped data
  useEffect(() => {
    if (!selectedBenchId) return;

    (async () => {
      try {
        setLoading(true);
        setError(null);
        await loadBenchScopedData(selectedBenchId);
      } catch (err: any) {
        setError(err.message ?? "Failed to load bench data.");
      } finally {
        setLoading(false);
      }
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedBenchId]);

  if (userLoading) {
    return (
      <div className="flex justify-center p-10">
        <p className="text-gray-500 animate-pulse">Loading user data...</p>
      </div>
    );
  }

  return (
    <>
      <Head>
        <title>Partnerships & Stakeholders</title>
      </Head>

      <Header />

      <div className="flex" data-cy="partnerships-layout">
        <SideNav />

        <main
          className="flex-1 p-6 mx-auto max-w-7xl"
          data-cy="partnerships-page"
        >
          <div className="space-y-8">
            <section data-cy="partnerships-map-section">
              <div className="flex flex-col sm:flex-row gap-3 sm:items-center sm:justify-between mb-4">
                <h1 className="text-3xl font-semibold">Partnerships</h1>

                <div className="flex items-center gap-2">
                  <span className="text-sm text-gray-600">Bench:</span>
                  <select
                    className="border rounded px-3 py-2 text-sm bg-white"
                    value={selectedBenchId ?? ""}
                    onChange={(e) => setSelectedBenchId(Number(e.target.value))}
                    disabled={benches.length === 0}
                    data-cy="bench-selector"
                  >
                    {benches.length === 0 ? (
                      <option value="">Geen benches</option>
                    ) : (
                      benches
                        .filter((b) => typeof b.id === "number")
                        .map((b) => (
                          <option key={b.id} value={b.id}>
                            {b.benchName ?? `Toestel ${b.id}`}{" "}
                            {b.city ? `(${b.city})` : ""}
                          </option>
                        ))
                    )}
                  </select>
                </div>
              </div>

              <div className="rounded-xl border border-gray-200 shadow-sm overflow-hidden">
                {loading ? (
                  <p className="p-4 text-center text-gray-500">Loading...</p>
                ) : error ? (
                  <p className="p-4 text-center text-red-500">Error: {error}</p>
                ) : (
                  <PartnershipsMap
                    categories={categories}
                    decidedCategoryIds={decidedCategoryIds}
                    onToggleCategory={handleToggleCategory}
                  />
                )}
              </div>
            </section>

            <section data-cy="partnerships-stakeholders-section">
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-semibold">Stakeholders</h2>

                <div className="flex gap-2">
                  <button
                    onClick={handleExport}
                    className="px-3 py-1 border rounded text-sm hover:bg-gray-50"
                    disabled={loading}
                  >
                    Export CSV
                  </button>

                  {isAllowed && (
                    <button
                      onClick={() => {
                        setEditing(null);
                        setShowForm(true);
                      }}
                      className="px-3 py-1 bg-green-600 text-white rounded text-sm hover:bg-green-700"
                    >
                      Voeg stakeholder toe
                    </button>
                  )}
                </div>
              </div>

              {showForm && isAllowed && (
                <div className="mb-4">
                  <StakeholderForm
                    initial={editing || undefined}
                    onSave={handleSave}
                    onCancel={() => {
                      setShowForm(false);
                      setEditing(null);
                    }}
                  />
                </div>
              )}

              {loading ? (
                <p className="p-4 text-center text-gray-500">Loading...</p>
              ) : error ? (
                <p className="p-4 text-center text-red-500">Error: {error}</p>
              ) : (
                <div className="rounded-xl border border-gray-200 shadow-sm bg-white">
                  <StakeholderList
                    stakeholders={stakeholders}
                    onEdit={(s) => {
                      setEditing(s);
                      setShowForm(true);
                    }}
                    onDelete={handleDelete}
                  />
                </div>
              )}
            </section>
          </div>
        </main>
      </div>
    </>
  );
};

export default StakeholdersPage;
