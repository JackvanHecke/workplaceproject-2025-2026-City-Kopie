"use client";

import React, { useCallback, useEffect, useState } from "react";
import CoachService from "../../services/CoachService";
import { Coach, Price } from "../../types";
import CoachesTable from "./CoachesTable";
import CoachEditModal from "./CoachEditModal";

const CoachesList: React.FC = () => {
  const [coaches, setCoaches] = useState<Coach[] | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [editing, setEditing] = useState<boolean>(false);
  const [editForm, setEditForm] = useState<Coach | null>(null);

  const fetchCoaches = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await CoachService.getAll();
      const contentType = res.headers.get("content-type");
      if (!res.ok) {
        const text = await res.text();
        throw new Error(`Failed: ${text}`);
      }
      if (!contentType?.includes("application/json")) {
        const text = await res.text();
        throw new Error(`Expected JSON, got: ${text.substring(0, 200)}`);
      }
      const body: Coach[] = await res.json();

      setCoaches(body);
    } catch (err: any) {
      setError(err?.message ?? "Failed to load coaches");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchCoaches();
  }, [fetchCoaches]);

  const mutate = async () => {
    await fetchCoaches();
  };

  const openEdit = (c: Coach) => {
    setEditForm(JSON.parse(JSON.stringify(c)));
    setEditing(true);
  };

  const saveEdit = (updated: Coach) => {
    if (!coaches) return;
    setCoaches((prev) =>
      prev
        ? prev.map((c) => (c.id === updated.id ? { ...c, ...updated, updatedAt: new Date().toISOString() } : c))
        : prev
    );
    setEditing(false);
    setEditForm(null);
  };

  const deleteCoach = (id?: number) => {
    if (!id) return;
    const ok = confirm("Delete this coach? This only affects the frontend mock data.");
    if (!ok) return;
    setCoaches((prev) => (prev ? prev.filter((c) => c.id !== id) : prev));
  };

  return (
    <section className="max-w-6xl mx-auto p-6" data-cy="coaches-list-section">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-semibold">Coaches </h1>
        <div className="flex items-center gap-3">
          <button
            onClick={mutate}
            className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300"
            aria-label="Refresh coaches"
            data-cy="refresh-coaches"
          >
            Refresh
          </button>
        </div>
      </div>

      {loading && <div className="text-center py-12">Loading coaches…</div>}
      {error && <div className="text-red-600 py-4">Error: {error}</div>}

      {!loading && !error && (
        <CoachesTable
          coaches={coaches ?? []}
          onEdit={openEdit}
          onDelete={deleteCoach}
        />
      )}

      {editing && editForm && (
        <CoachEditModal
          coach={editForm}
          onClose={() => {
            setEditing(false);
            setEditForm(null);
          }}
          onSave={(c) => saveEdit(c)}
        />
      )}
    </section>
  );
};

export default CoachesList;
