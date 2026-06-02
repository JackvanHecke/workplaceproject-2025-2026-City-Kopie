import React, { useEffect, useState } from "react";
import { Location, MovementRoute } from "../../types";
import MovementRouteService from "../../services/MovementRouteService";
import LocationService from "../../services/LocationService";

type Props = {
  bench: Location;
  onBenchUpdated?: (bench: Location) => void;
};

const MovementRouteTab: React.FC<Props> = ({ bench, onBenchUpdated }) => {
  const [route, setRoute] = useState<MovementRoute>({
    id: bench.movementRouteId,
    name: bench.movementRouteName || "",
    type: bench.movementRouteType || "",
  });

  const [file, setFile] = useState<File | null>(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    setRoute({
      id: bench.movementRouteId,
      name: bench.movementRouteName || "",
      type: bench.movementRouteType || "",
    });
  }, [bench]);

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!bench.id) return;

    try {
      setSaving(true);

      let savedRoute: MovementRoute;
      if (route.id) {
        savedRoute = await MovementRouteService.update(route.id, {
          name: route.name,
          type: route.type,
        });
      } else {
        savedRoute = await MovementRouteService.create({
          name: route.name,
          type: route.type,
        });
      }

      if (file) {
        console.log("Route file selected (GPX/GeoJSON/etc):", file);
      }

      const updatedBench = await LocationService.update(bench.id!, {
        movementRouteId: savedRoute.id,
      });

      onBenchUpdated?.(updatedBench);
    } catch (err) {
      console.error("Failed to save movement route", err);
      alert("Saving movement route failed – see console.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <form className="space-y-6" onSubmit={handleSave}>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">
            Naam beweegroute
          </label>
          <input
            className="w-full border rounded p-2"
            value={route.name ?? ""}
            onChange={(e) => setRoute((r) => ({ ...r, name: e.target.value }))}
            placeholder="vb. Parklus Leuven"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">
            Type beweegroute
          </label>
          <input
            className="w-full border rounded p-2"
            value={route.type ?? ""}
            onChange={(e) => setRoute((r) => ({ ...r, type: e.target.value }))}
            placeholder="vb. Wandelroute, Looproute..."
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
        <div className="border rounded h-64 flex items-center justify-center bg-gray-50 text-gray-500 text-sm">
          Map preview (TODO: integrate real map using coordinates / route data)
        </div>

        <div>
          <label className="block text-sm font-medium mb-2">
            Importeer routebestand
          </label>
          <input
            type="file"
            accept=".gpx,.geojson,.json"
            onChange={(e) => setFile(e.target.files?.[0] ?? null)}
            className="block w-full text-sm"
          />
          {file && (
            <p className="mt-2 text-xs text-gray-600">
              Geselecteerd bestand: <strong>{file.name}</strong>
            </p>
          )}
          <p className="mt-2 text-xs text-gray-500">
            (Deze import is voorlopig enkel UI – backend endpoint kan later
            gekoppeld worden.)
          </p>
        </div>
      </div>

      <div className="flex justify-end gap-2 mt-6">
        <button
          type="submit"
          disabled={saving}
          className="px-4 py-2 bg-blue-600 text-white rounded"
        >
          {saving ? "Opslaan..." : "Beweegroute opslaan"}
        </button>
      </div>
    </form>
  );
};

export default MovementRouteTab;
