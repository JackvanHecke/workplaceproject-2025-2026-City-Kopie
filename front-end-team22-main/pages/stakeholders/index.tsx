// pages/stakeholders/index.tsx
import React, { useEffect, useState } from "react";
import StakeholderList from "@components/stakeholders/StakeholderList";
import { Stakeholder } from "@types";
import StakeholderService from "@services/StakeholderService";
import Header from "@components/header";
import Head from "next/head";
import StakeholderForm from "@components/stakeholders/StakeholderForm";
import exportToCsv from "utils/exportToCsv";

const StakeholdersPage: React.FC = () => {
  const [stakeholders, setStakeholders] = useState<Stakeholder[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<Stakeholder | null>(null);

  const fetchStakeholders = async () => {
    try {
      setLoading(true);
      const data = await StakeholderService.getAll();
      setStakeholders(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStakeholders();
  }, []);

  const handleSave = async (s: Stakeholder) => {
    try {
      await StakeholderService.create(s);
      fetchStakeholders();
      setShowForm(false);
    } catch (err: any) {
      alert("Error saving stakeholder: " + err.message);
    }
  };

  const handleDelete = async (id: number) => {
    if (!id) return;
    if (!confirm("Are you sure you want to delete this stakeholder?")) return;
    try {
      await StakeholderService.delete(id);
      await fetchStakeholders();
    } catch (err: any) {
      alert("Error deleting stakeholder: " + err.message);
    }
  };

  const handleExport = () => {
    exportToCsv(stakeholders, "stakeholders.csv");
  };

  const onCreateClick = () => {
    setEditing(null);
    setShowForm(true);
  };

  return (
    <>
      <Head>
        <title>Stakeholders</title>
      </Head>
      <Header />
      <main
        className="mx-auto md:w-4/5 lg:w-1/2 p-4"
        data-cy="stakeholders-page"
      >
        <div className="space-y-4">
          <div
            className="flex justify-between items-center"
            data-cy="stakeholders-header"
          >
            <h2 className="text-2xl" data-cy="stakeholders-heading">
              Stakeholders
            </h2>
            <div className="flex gap-2">
              <button
                onClick={handleExport}
                className="px-3 py-1 border rounded"
                data-cy="stakeholders-export-button"
              >
                Export CSV
              </button>
              <button
                onClick={() => onCreateClick()}
                className="px-3 py-1 bg-green-600 text-white rounded"
                data-cy="stakeholders-add-button"
              >
                Add Stakeholder
              </button>
            </div>
          </div>

          {showForm && (
            <div data-cy="stakeholder-form-wrapper">
              <StakeholderForm
                initial={editing || undefined}
                onSave={handleSave}
                onCancel={() => setShowForm(false)}
              />
            </div>
          )}

          {loading ? (
            <p
              className="p-4 text-center text-gray-500"
              data-cy="stakeholders-loading"
            >
              Loading...
            </p>
          ) : error ? (
            <p
              className="p-4 text-center text-red-500"
              data-cy="stakeholders-error"
            >
              Error: {error}
            </p>
          ) : (
            <div data-cy="stakeholders-table-wrapper">
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
        </div>
      </main>
    </>
  );
};

export default StakeholdersPage;
