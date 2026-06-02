import React from "react";
import { Device } from "@types";
import { useRouter } from "next/router";

type Props = {
  devices: Device[];
};

const DeviceList: React.FC<Props> = ({ devices }) => {
  const router = useRouter();

  const handleRowClick = (device: Device) => {
    localStorage.clear();
    localStorage.setItem("selectedDevice", JSON.stringify(device));

    router.push(`/dashboardklant`);
  };

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-left border-collapse">
        <thead>
          <tr className="bg-gray-100">
            <th className="py-2">Id</th>
            <th className="py-2">Region</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((d) => (
            <tr
              onClick={() => handleRowClick(d)}
              key={d.id}
              className="border-t cursor-pointer hover:bg-gray-50"
            >
              <td className="p-2">{d.id}</td>
              <td className="p-2">{d.region}</td>
            </tr>
          ))}
          {devices.length === 0 && (
            <tr>
              <td colSpan={2} className="p-4 text-center text-gray-500">
                No devices found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default DeviceList;
