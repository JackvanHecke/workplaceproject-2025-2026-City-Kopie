"use client";

import React, { useEffect, useMemo, useState } from "react";
import { Location } from "../../types";
import LocationService from "../../services/LocationService";
import BenchListView from "../../components/bench/BenchListView";
import { useUser } from "utils/UserContext";
import { useRouter } from "next/navigation";

const BenchesSection: React.FC = () => {
  const [locations, setLocations] = useState<Location[]>([]);
  const [loading, setLoading] = useState(true);

  const [query, setQuery] = useState("");
  const [perPage, setPerPage] = useState(10);
  const [page, setPage] = useState(1);

  const router = useRouter();
  const { currentUser, loading: userLoading } = useUser();

  const role = (currentUser?.role || "").toUpperCase();
  const isAdmin = role === "ADMIN";
  const isClient = role === "CLIENT";
  const isCoach = role === "COACH";
  const isEndUser = role === "END_USER";

const load = async () => {
  if (!currentUser) return;

  setLoading(true);
  try {
    let data: Location[] = [];

    // ADMIN, COACH en END_USER zien alle locaties. 
    // CLIENT ziet alleen eigen locaties.
    if (isAdmin || isCoach || isEndUser) {
      data = await LocationService.getAll();
    } else if (isClient && currentUser.email) {
      data = await LocationService.getMine(currentUser.email);
    }

    setLocations(Array.isArray(data) ? data : []);
  } catch (err) {
    console.error("Failed to load locations:", err);
    setLocations([]);
  } finally {
    setLoading(false);
  }
};
  useEffect(() => {
    if (userLoading) return;

    if (!currentUser) {
      router.push("/login");
      return;
    }

    load();
  }, [userLoading, currentUser]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return locations;

    return locations.filter((b) =>
      [
        b.benchName,
        b.type,
        b.size,
        b.owner,
        b.street,
        b.houseNumber,
        b.zipCode,
        b.city,
        b.country,
        (b.tags ?? []).join(" "),
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase()
        .includes(q)
    );
  }, [locations, query]);

  const total = filtered.length;
  const totalPages = Math.max(1, Math.ceil(total / perPage));
  const currentPage = page > totalPages ? 1 : page;

  const pageItems = filtered.slice(
    (currentPage - 1) * perPage,
    currentPage * perPage
  );

  const handleDelete = async (loc: Location) => {
    if (!confirm(`Delete location ${loc.benchName ?? loc.id}?`)) return;

    try {
      await LocationService.remove(loc.id!);
      setLocations((prev) => prev.filter((x) => x.id !== loc.id));
    } catch (err: any) {
      alert("Delete failed: " + (err.message ?? err));
    }
  };

  const handleEdit = (loc: Location) => {
    router.push(`/infrastructuur/registerBench/${loc.id}`);
  };

  if (userLoading) {
    return <p>Loading user...</p>;
  }

  return (
    <section className="space-y-4">
      <h2 className="text-2xl font-bold">Beweegtoestellen</h2>

      <div className="flex flex-col md:flex-row gap-3 justify-between">
        <input
          placeholder="Search..."
          value={query}
          onChange={(e) => {
            setQuery(e.target.value);
            setPage(1);
          }}
          className="p-2 border rounded w-full md:w-1/2"
        />

        <div className="flex items-center gap-2">
          <label className="text-sm">Per page</label>
          <select
            value={perPage}
            onChange={(e) => {
              setPerPage(Number(e.target.value));
              setPage(1);
            }}
            className="p-2 border rounded"
          >
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>
      </div>



      {loading ? (
        <p>Loading benches...</p>
      ) : (
        <>
          <BenchListView
            benches={pageItems}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />

          <div className="flex justify-between items-center mt-4">
            <span className="text-sm text-gray-600">
              Showing {pageItems.length} of {total}
            </span>

            <div className="flex gap-2">
              <button
                disabled={page <= 1}
                onClick={() => setPage((p) => p - 1)}
                className="px-3 py-1 border rounded"
              >
                Prev
              </button>

              <span className="px-3 py-1 border rounded">
                {currentPage} / {totalPages}
              </span>

              <button
                disabled={page >= totalPages}
                onClick={() => setPage((p) => p + 1)}
                className="px-3 py-1 border rounded"
              >
                Next
              </button>
            </div>
          </div>
        </>
      )}
    </section>
  );
};

export default BenchesSection;