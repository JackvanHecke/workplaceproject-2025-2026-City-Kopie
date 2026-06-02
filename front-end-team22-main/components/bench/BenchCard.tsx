// components/bench/BenchCard.tsx
import React, { useState } from "react";
import { Location } from "../../types";
import LocationService from "../../services/LocationService";

type Props = {
  bench: Location;
  onUpdated?: (b: Location) => void;
  onDeleted?: (id?: number) => void;
};

const BenchCard: React.FC<Props> = ({ bench, onUpdated, onDeleted }) => {
  const [loadingValidate, setLoadingValidate] = useState(false);

  const handleValidate = async () => {
    setLoadingValidate(true);
    try {
      const updated = await LocationService.update(bench.id!, {
        tags: bench.tags,
      });
      onUpdated?.(updated);
    } catch (err: any) {
      console.error(err);
      alert("Validation failed: " + err.message);
    } finally {
      setLoadingValidate(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm(`Delete ${bench.benchName}?`)) return;
    try {
      await LocationService.remove(bench.id!);
      onDeleted?.(bench.id);
    } catch (err: any) {
      console.error(err);
      alert("Delete failed: " + err.message);
    }
  };

  return (
    <div className="p-3 border rounded shadow-sm bg-white flex gap-3">
      {bench.photoUrl && (
        <img
          src={bench.photoUrl}
          alt={bench.benchName ?? "Bench"}
          className="w-24 h-24 object-cover rounded flex-shrink-0"
        />
      )}

      <div className="flex-1">
        <div className="flex justify-between items-start">
          <div>
            <div className="text-lg font-semibold">{bench.benchName}</div>
            <div className="text-sm text-gray-600">
              {[bench.street, bench.city, bench.country]
                .filter(Boolean)
                .join(", ")}
            </div>
            {bench.tags && bench.tags.length > 0 && (
              <div className="text-sm mt-1">Tags: {bench.tags.join(", ")}</div>
            )}
          </div>

          <div className="flex flex-col items-end gap-2">
            <button
              onClick={handleValidate}
              disabled={loadingValidate}
              className="px-2 py-1 rounded bg-indigo-600 text-white text-sm"
            >
              {loadingValidate ? "Validating..." : "Validate"}
            </button>
            <button
              onClick={handleDelete}
              className="px-2 py-1 rounded bg-red-600 text-white text-sm"
            >
              Delete
            </button>
          </div>
        </div>

        <div className="mt-2 text-xs text-gray-500 flex gap-3 flex-wrap">
          {bench.showInApp && <span>In app</span>}
          {bench.mobile && <span>Mobile</span>}
          {bench.publicAvailable && <span>Public</span>}
        </div>
      </div>
    </div>
  );
};

export default BenchCard;
