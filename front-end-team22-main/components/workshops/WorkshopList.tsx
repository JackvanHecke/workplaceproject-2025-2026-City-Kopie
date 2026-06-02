import React, { useState, useEffect } from "react";
import { Workshop, Location } from "@types";

type Props = {
  workshops: Workshop[];
};

const STORAGE_KEY = "workshop-registrations";

const WorkshopList: React.FC<Props> = ({ workshops }) => {
  const [expanded, setExpanded] = useState<number | null>(null);

  const [registrations, setRegistrations] = useState<Record<number, boolean>>(
    () => {
      if (typeof window === "undefined") return {};
      try {
        const raw = window.localStorage.getItem(STORAGE_KEY);
        return raw ? (JSON.parse(raw) as Record<number, boolean>) : {};
      } catch {
        return {};
      }
    }
  );

  useEffect(() => {
    try {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(registrations));
    } catch {
      // ignore write errors
    }
  }, [registrations]);

  const handleParticipate = (id: number) => {
    setRegistrations((prev) => ({ ...prev, [id]: true }));
  };

  const handleUnparticipate = (id: number) => {
    setRegistrations((prev) => {
      const updated = { ...prev };
      delete updated[id];
      return updated;
    });
  };

  const renderLocation = (location?: Location) => {
    if (!location) return "Unknown";
    return `${location.benchName}`;
  };

  return (
    <div className="overflow-x-auto rounded-xl shadow-md">
      <table className="min-w-full border border-gray-200">
        <thead className="bg-client-primary text-white">
          <tr>
            <th className="py-3 px-4 text-left text-sm font-semibold w-16">
              ID
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold">Name</th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-28">
              Start
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-28">
              End
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-40">
              Location
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-24">
              Price
            </th>
          </tr>
        </thead>

        <tbody className="bg-white divide-y divide-gray-200">
          {workshops.length === 0 && (
            <tr>
              <td colSpan={6} className="p-6 text-center text-gray-500 text-sm">
                No workshops found.
              </td>
            </tr>
          )}

          {workshops.map((w, index) => {
            const isRegistered = !!registrations[w.id];
            const isExpanded = expanded === w.id;

            return (
              <React.Fragment key={w.id}>
                {/* Main row */}
                <tr
                  className={`transition-all duration-150 ${
                    index % 2 === 0 ? "bg-white" : "bg-gray-50"
                  } hover:bg-gray-100 cursor-pointer`}
                  onClick={() => setExpanded(isExpanded ? null : w.id)}
                >
                  <td className="py-3 px-4 text-sm text-gray-700">{w.id}</td>

                  <td className="py-3 px-4">
                    <div className="flex items-center gap-2">
                      {isRegistered ? (
                        <span className="text-green-600 text-base font-bold flex-shrink-0">
                          ✓
                        </span>
                      ) : (
                        <span className="text-gray-300 text-base flex-shrink-0">
                          ○
                        </span>
                      )}
                      <span className="font-medium text-gray-800 text-sm">
                        {w.naam}
                      </span>
                    </div>
                  </td>

                  <td className="py-3 px-4 text-sm text-gray-700">
                    {w.startDatum}
                  </td>
                  <td className="py-3 px-4 text-sm text-gray-700">
                    {w.eindDatum}
                  </td>
                  <td className="py-3 px-4 text-sm text-gray-700">
                    {renderLocation(w.location)}
                  </td>
                  <td className="py-3 px-4 text-sm font-semibold text-gray-900">
                    €{w.prijs}
                  </td>
                </tr>

                {/* Expanded row */}
                {isExpanded && (
                  <tr className={index % 2 === 0 ? "bg-white" : "bg-gray-50"}>
                    <td
                      colSpan={6}
                      className="px-4 py-4 border-t border-gray-100"
                    >
                      <div className="space-y-3">
                        {/* Action buttons */}
                        <div className="flex flex-wrap items-center gap-2">
                          {!isRegistered ? (
                            <button
                              onClick={(e) => {
                                e.stopPropagation();
                                handleParticipate(w.id);
                              }}
                              className="px-4 py-1.5 bg-client-primary text-white rounded-md hover:bg-darker-client-bg transition-colors duration-150 text-sm font-medium"
                            >
                              Participate
                            </button>
                          ) : (
                            <>
                              <span className="px-3 py-1 rounded-full bg-green-100 text-green-800 text-xs font-semibold">
                                Registered
                              </span>

                              <button
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleUnparticipate(w.id);
                                }}
                                className="px-4 py-1.5 bg-red-500 text-white rounded-md hover:bg-red-600 transition-colors duration-150 text-sm font-medium"
                              >
                                Unparticipate
                              </button>
                            </>
                          )}

                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              setExpanded(null);
                            }}
                            className="px-4 py-1.5 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-100 transition-colors duration-150 text-sm font-medium"
                          >
                            Close
                          </button>
                        </div>

                        {/* Workshop details */}
                        <div className="text-sm space-y-1.5 text-gray-600">
                          <p>
                            <span className="font-semibold text-gray-700">
                              Target group:
                            </span>{" "}
                            {w.doelgroep}
                          </p>
                          <p>
                            <span className="font-semibold text-gray-700">
                              Organizer:
                            </span>{" "}
                            {w.organisator}
                          </p>
                          <p>
                            <span className="font-semibold text-gray-700">
                              Contact:
                            </span>{" "}
                            {w.contactPersoon}
                          </p>
                        </div>
                      </div>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default WorkshopList;
