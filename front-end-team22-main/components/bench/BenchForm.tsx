import React, { useEffect, useMemo, useState } from "react";
import { Location } from "../../types";

type BenchFormProps = {
  bench: Location;
  onSubmit: (payload: any, photoFile?: File | null) => void;
  onCancel?: () => void;
};

const toArray = (value: any): string[] => {
  if (Array.isArray(value)) {
    return value.map((v) => String(v).trim()).filter(Boolean);
  }
  if (typeof value === "string") {
    return value
      .split(",")
      .map((t) => t.trim())
      .filter((t) => t.length > 0);
  }
  return [];
};

const STATION_OPTIONS: string[] = [
  "push-up bench",
  "pull-up bench",
  "dips bench",
  "step-up bench",
  "core bench",
];

const BenchForm: React.FC<BenchFormProps> = ({ bench, onSubmit, onCancel }) => {
  const [name, setName] = useState(bench.benchName ?? "");
  const [type, setType] = useState(bench.type ?? "bench"); // enum: bench | wall
  const [size, setSize] = useState(bench.size ?? "");
  const [owner, setOwner] = useState(bench.owner ?? "");
  const [street, setStreet] = useState(bench.street ?? "");
  const [houseNumber, setHouseNumber] = useState(bench.houseNumber ?? "");
  const [zipCode, setZipCode] = useState(bench.zipCode ?? "");
  const [city, setCity] = useState(bench.city ?? "");
  const [country, setCountry] = useState(bench.country ?? "");

  const [showInApp, setShowInApp] = useState(bench.showInApp ?? true);
  const [mobile, setMobile] = useState(bench.mobile ?? false);
  const [publicAvailable, setPublicAvailable] = useState(
    bench.publicAvailable ?? true
  );

  const [latitude, setLatitude] = useState(
    bench.latitude !== undefined ? String(bench.latitude) : ""
  );
  const [longitude, setLongitude] = useState(
    bench.longitude !== undefined ? String(bench.longitude) : ""
  );

  const initialTagsString = useMemo(
    () => toArray(bench.tags).join(", "),
    [bench.tags]
  );
  const [tagsInput, setTagsInput] = useState(initialTagsString);

  const [selectedStations, setSelectedStations] = useState<string[]>(
    bench.stations ?? []
  );

  const [photoFile, setPhotoFile] = useState<File | null>(null);

  useEffect(() => {
    setName(bench.benchName ?? "");
    setType(bench.type ?? "bench");
    setSize(bench.size ?? "");
    setOwner(bench.owner ?? "");
    setStreet(bench.street ?? "");
    setHouseNumber(bench.houseNumber ?? "");
    setZipCode(bench.zipCode ?? "");
    setCity(bench.city ?? "");
    setCountry(bench.country ?? "");
    setShowInApp(bench.showInApp ?? true);
    setMobile(bench.mobile ?? false);
    setPublicAvailable(bench.publicAvailable ?? true);

    setLatitude(bench.latitude !== undefined ? String(bench.latitude) : "");
    setLongitude(bench.longitude !== undefined ? String(bench.longitude) : "");
    setTagsInput(initialTagsString);
    setSelectedStations(bench.stations ?? []);
    setPhotoFile(null);
  }, [bench, initialTagsString]);

  const tagsArray = useMemo(() => toArray(tagsInput), [tagsInput]);

  const toggleStation = (station: string) => {
    setSelectedStations((prev) =>
      prev.includes(station)
        ? prev.filter((s) => s !== station)
        : [...prev, station]
    );
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] ?? null;
    setPhotoFile(file);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const latNum =
      latitude.trim() === "" ? undefined : Number.parseFloat(latitude);
    const lngNum =
      longitude.trim() === "" ? undefined : Number.parseFloat(longitude);

    const payload = {
      name: name || undefined,
      type: type || undefined,
      size: size || undefined,
      owner: owner || undefined,
      street: street || undefined,
      houseNumber: houseNumber || undefined,
      zipCode: zipCode || undefined,
      city: city || undefined,
      country: country || undefined,
      tags: tagsInput || undefined,
      showInApp,
      mobile,
      publicAvailable,
      latitude:
        latNum !== undefined && !Number.isNaN(latNum) ? latNum : undefined,
      longitude:
        lngNum !== undefined && !Number.isNaN(lngNum) ? lngNum : undefined,
      stations: selectedStations.join(","),
    };

    onSubmit(payload, photoFile);
  };

  return (
    <form className="space-y-6" onSubmit={handleSubmit}>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">Name</label>
          <input
            className="w-full border rounded p-2"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Bench name"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Type</label>
          <select
            className="w-full border rounded p-2"
            value={type}
            onChange={(e) => setType(e.target.value)}
          >
            <option value="bench">Bench</option>
            <option value="wall">Wall</option>
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Size</label>
          <input
            className="w-full border rounded p-2"
            value={size}
            onChange={(e) => setSize(e.target.value)}
            placeholder="Size"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Owner</label>
          <input
            className="w-full border rounded p-2"
            value={owner}
            onChange={(e) => setOwner(e.target.value)}
            placeholder="Owner / organisation"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Street</label>
          <input
            className="w-full border rounded p-2"
            value={street}
            onChange={(e) => setStreet(e.target.value)}
            placeholder="Street"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">House number</label>
          <input
            className="w-full border rounded p-2"
            value={houseNumber}
            onChange={(e) => setHouseNumber(e.target.value)}
            placeholder="e.g. 12B"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Zip code</label>
          <input
            className="w-full border rounded p-2"
            value={zipCode}
            onChange={(e) => setZipCode(e.target.value)}
            placeholder="e.g. 3000"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">City</label>
          <input
            className="w-full border rounded p-2"
            value={city}
            onChange={(e) => setCity(e.target.value)}
            placeholder="City"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">Country</label>
          <input
            className="w-full border rounded p-2"
            value={country}
            onChange={(e) => setCountry(e.target.value)}
            placeholder="Country"
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">
          Tags (comma-separated)
        </label>
        <input
          className="w-full border rounded p-2"
          value={tagsInput}
          onChange={(e) => setTagsInput(e.target.value)}
          placeholder="e.g. park, city-centre, beginner-friendly"
        />
        <div className="mt-2 flex gap-2 flex-wrap">
          {tagsArray.map((t) => (
            <span key={t} className="px-2 py-1 bg-gray-200 rounded text-sm">
              {t}
            </span>
          ))}
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium mb-2">
          Stations (exercise types)
        </label>
        <div className="flex flex-wrap gap-2">
          {STATION_OPTIONS.map((opt) => (
            <label
              key={opt}
              className="inline-flex items-center gap-1 border rounded px-2 py-1 text-sm cursor-pointer"
            >
              <input
                type="checkbox"
                className="accent-blue-600"
                checked={selectedStations.includes(opt)}
                onChange={() => toggleStation(opt)}
              />
              <span>{opt}</span>
            </label>
          ))}
        </div>
      </div>

      <div className="flex flex-wrap gap-4">
        <label className="inline-flex items-center gap-2 text-sm">
          <input
            id="showInApp"
            type="checkbox"
            checked={showInApp}
            onChange={(e) => setShowInApp(e.target.checked)}
            className="accent-blue-600"
          />
          <span>Show this bench in the app</span>
        </label>

        <label className="inline-flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={mobile}
            onChange={(e) => setMobile(e.target.checked)}
            className="accent-blue-600"
          />
          <span>Bench is mobile</span>
        </label>

        <label className="inline-flex items-center gap-2 text-sm">
          <input
            type="checkbox"
            checked={publicAvailable}
            onChange={(e) => setPublicAvailable(e.target.checked)}
            className="accent-blue-600"
          />
          <span>Publicly available</span>
        </label>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">Latitude</label>
          <input
            className="w-full border rounded p-2"
            value={latitude}
            onChange={(e) => setLatitude(e.target.value)}
            placeholder="e.g. 50.8798"
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Longitude</label>
          <input
            className="w-full border rounded p-2"
            value={longitude}
            onChange={(e) => setLongitude(e.target.value)}
            placeholder="e.g. 4.7005"
          />
        </div>
      </div>

      {bench.movementRouteName && (
        <div className="p-3 border rounded bg-gray-50 text-sm">
          <div className="font-medium mb-1">Movement route</div>
          <div>Name: {bench.movementRouteName}</div>
          {bench.movementRouteType && (
            <div>Type: {bench.movementRouteType}</div>
          )}
          {bench.movementRouteId && <div>ID: {bench.movementRouteId}</div>}
        </div>
      )}

      <div>
        <label className="block text-sm font-medium mb-1">Bench photo</label>

        {bench.photoUrl && (
          <div className="mb-2">
            <div className="text-xs text-gray-500 mb-1">Current photo</div>
            <img
              src={bench.photoUrl}
              alt={bench.benchName ?? "Bench"}
              className="h-32 w-full max-w-xs object-cover rounded border"
            />
          </div>
        )}

        <input
          type="file"
          accept="image/*"
          onChange={handleFileChange}
          className="block text-sm"
        />
        {photoFile && (
          <p className="mt-1 text-xs text-gray-600">
            Selected: {photoFile.name}
          </p>
        )}
      </div>

      <div className="flex gap-2 justify-end mt-4">
        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 border rounded"
          >
            Cancel
          </button>
        )}
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 text-white rounded"
        >
          Save
        </button>
      </div>
    </form>
  );
};

export default BenchForm;
