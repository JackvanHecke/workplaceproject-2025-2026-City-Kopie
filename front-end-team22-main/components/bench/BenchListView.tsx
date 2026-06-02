"use client";

import React, { useEffect, useState } from "react";
import { Location } from "../../types";
import { useRouter } from "next/navigation";
import { useUser } from "utils/UserContext";

type Props = {
  benches: Location[];
  onEdit: (bench: Location) => void;
  onDelete: (bench: Location) => void;
};

const BenchListView: React.FC<Props> = ({ benches, onEdit, onDelete }) => {
  const router = useRouter();
  const { currentUser, loading } = useUser();
  const [selected, setSelected] = useState<Location | null>(null);

  useEffect(() => {
    if (!currentUser && !loading) {
      router.push("/login");
    }
  }, [currentUser, loading, router]);

  const role = (currentUser?.role || "").toUpperCase();
  const isAdmin = role === "ADMIN";
  const isClient = role === "CLIENT";
  const isCoach = role === "COACH";
  const isEndUser = role === "END_USER";

  // Alleen Admins en Clients mogen doorklikken naar het dashboard of acties uitvoeren
  const canManage = isAdmin || isClient;

  const handleRowClick = (location: Location) => {
    if (!canManage) return;
    localStorage.clear();
    localStorage.setItem("selectedLocation", JSON.stringify(location));
    router.push("/dashboardklant");
  };

  const flagText = (value?: boolean | null) =>
    value == null ? "-" : value ? "Yes" : "No";

  if (loading) {
    return (
      <div className="flex justify-center p-10">
        <p className="text-gray-500 animate-pulse">Loading user data...</p>
      </div>
    );
  }

  return (
    <div className="w-full space-y-4">
      <div className="overflow-x-auto">
        <table className="min-w-full border border-gray-200 rounded-xl shadow-md overflow-hidden">
          <thead className="bg-client-primary text-white">
            <tr>
              <th className="p-3 text-left">Number</th>
              <th className="p-3 text-left">Straat</th>
              <th className="p-3 text-left">Stad</th>
              <th className="p-3 text-left">Land</th>
              <th className="p-3 text-left">Tags</th>
              <th className="p-3 text-center">Mobiel</th>
              <th className="p-3 text-center">Openbaar</th>
              <th className="p-3 text-center">In App</th>
              <th className="p-3 text-center">Details</th>
              {canManage && <th className="p-3 text-center">Acties</th>}
            </tr>
          </thead>

          <tbody className="divide-y">
            {benches.map((b, index) => (
              <tr
                key={b.id}
                onClick={() => handleRowClick(b)}
                className={`transition ${canManage ? "cursor-pointer hover:bg-gray-100" : "cursor-default"
                  } ${index % 2 === 0 ? "bg-gray-50" : "bg-white"}`}
              >
                <td className="p-3">#{b.id}</td>
                <td className="p-3">
                  {[b.street, b.houseNumber].filter(Boolean).join(" ")}
                </td>
                <td className="p-3">{b.city}</td>
                <td className="p-3">{b.country}</td>
                <td className="p-3 italic text-gray-500">
                  {b.tags?.join(", ") || "No tags"}
                </td>
                <td className="p-3 text-center">{flagText(b.mobile)}</td>
                <td className="p-3 text-center">{flagText(b.publicAvailable)}</td>
                <td className="p-3 text-center">{flagText(b.showInApp)}</td>
                <td className="p-3 text-center">
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      setSelected(b);
                    }}
                    className="px-3 py-1 bg-gray-900 text-white rounded text-sm hover:bg-gray-700"
                  >
                    View
                  </button>
                </td>

                {canManage && (
                  <td className="p-3 text-center space-x-2">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleRowClick(b);
                      }}
                      className="px-3 py-1 bg-green-600 text-white rounded text-sm hover:bg-green-700"
                    >
                      Dashboard
                    </button>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        onEdit(b);
                      }}
                      className="px-3 py-1 bg-blue-600 text-white rounded text-sm hover:bg-blue-700"
                    >
                      Bewerk
                    </button>
                    {isAdmin && (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          onDelete(b);
                        }}
                        className="px-3 py-1 bg-red-600 text-white rounded text-sm hover:bg-red-700"
                      >
                        Verwijder
                      </button>
                    )}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selected && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center"
          role="dialog"
          aria-modal="true"
          onClick={() => setSelected(null)}
        >
          <div className="absolute inset-0 bg-black/40" />
          <div
            className="relative z-10 w-[min(920px,95vw)] max-h-[85vh] overflow-y-auto rounded-xl bg-white shadow-xl border border-gray-200"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="p-4 md:p-6 border-b flex items-start justify-between gap-4">
              <div>
                <h3 className="text-xl font-semibold text-gray-900">
                  {selected.benchName ?? "Bench details"}
                </h3>
                <p className="text-sm text-gray-600 mt-1">
                  {[selected.street, selected.houseNumber, selected.city, selected.country]
                    .filter(Boolean)
                    .join(", ")}
                </p>
              </div>
              <button
                onClick={() => setSelected(null)}
                className="px-3 py-1 border rounded text-sm hover:bg-gray-50"
              >
                Close
              </button>
            </div>
            <div className="p-4 md:p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="md:col-span-1">
                {selected.photoUrl ? (
                  <img
                    src={selected.photoUrl}
                    alt={selected.benchName ?? "Bench"}
                    className="w-full h-56 object-cover rounded-lg border"
                  />
                ) : (
                  <div className="w-full h-56 rounded-lg border bg-gray-50 flex items-center justify-center text-gray-400 text-sm">
                    No photo
                  </div>
                )}
              </div>
              <div className="md:col-span-2 space-y-4">
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm">
                  <div><span className="font-medium">Owner:</span> {selected.owner || "-"}</div>
                  <div><span className="font-medium">Type:</span> {selected.type || "-"}</div>
                  <div><span className="font-medium">Size:</span> {selected.size || "-"}</div>
                  <div><span className="font-medium">Connected routes:</span> {selected.connectedRoutes ?? "-"}</div>
                  <div><span className="font-medium">Mobile:</span> {flagText(selected.mobile)}</div>
                  <div><span className="font-medium">Public:</span> {flagText(selected.publicAvailable)}</div>
                  <div><span className="font-medium">Show in app:</span> {flagText(selected.showInApp)}</div>
                  <div>
                    <span className="font-medium">Coordinates:</span>
                    {selected.latitude != null && selected.longitude != null
                      ? `${selected.latitude}, ${selected.longitude}`
                      : "-"}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BenchListView;