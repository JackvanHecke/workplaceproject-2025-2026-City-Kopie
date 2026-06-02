import React from "react";
import { Stakeholder } from "@types";

type Props = {
  stakeholders: Stakeholder[];
  onEdit: (s: Stakeholder) => void;
  onDelete: (id: number) => void;
};

const StakeholderList: React.FC<Props> = ({
  stakeholders,
  onEdit,
  onDelete,
}) => {
  return (
    <div
      className="overflow-x-auto rounded-xl shadow-md"
      data-cy="stakeholder-list-container"
    >
      <table
        className="min-w-full border border-gray-200"
        data-cy="stakeholder-table"
      >
        <thead className="bg-client-primary text-white">
          <tr>
            <th className="py-3 px-4 text-left text-sm font-semibold w-16">
              Id
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold">
              Organisation
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold">
              Contact Person
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold">Email</th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-32">
              Phone
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-32">
              Role
            </th>
            <th className="py-3 px-4 text-left text-sm font-semibold w-40">
              Actions
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {stakeholders.map((s, index) => (
            <tr
              key={s.id}
              className={`transition-all duration-150 ${
                index % 2 === 0 ? "bg-white" : "bg-client-bg"
              } hover:bg-client-bg/50`}
              data-cy={`stakeholder-row-${s.id}`}
            >
              <td
                className="py-3 px-4 text-sm text-gray-700"
                data-cy={`stakeholder-id-${s.id}`}
              >
                {s.id}
              </td>
              <td
                className="py-3 px-4 text-sm font-medium text-gray-800"
                data-cy={`stakeholder-organisation-${s.id}`}
              >
                {s.organisation}
              </td>
              <td
                className="py-3 px-4 text-sm text-gray-700"
                data-cy={`stakeholder-contact-${s.id}`}
              >
                {s.contactPerson}
              </td>
              <td
                className="py-3 px-4 text-sm text-gray-700"
                data-cy={`stakeholder-email-${s.id}`}
              >
                {s.email}
              </td>
              <td
                className="py-3 px-4 text-sm text-gray-700"
                data-cy={`stakeholder-phone-${s.id}`}
              >
                {s.phoneNumber}
              </td>
              <td
                className="py-3 px-4 text-sm text-gray-700"
                data-cy={`stakeholder-role-${s.id}`}
              >
                {s.role}
              </td>
              <td className="py-3 px-4">
                <div className="flex gap-2">
                  <button
                    onClick={() => onEdit(s)}
                    className="px-4 py-1.5 bg-client-primary text-white rounded-md hover:bg-darker-client-bg transition-colors duration-150 text-sm font-medium"
                    data-cy={`stakeholder-edit-button-${s.id}`}
                  >
                    Edit
                  </button>
                  <button
                    onClick={() => onDelete(s.id!)}
                    className="px-4 py-1.5 border border-red-300 rounded-md text-red-600 hover:bg-red-50 transition-colors duration-150 text-sm font-medium"
                    data-cy={`stakeholder-delete-button-${s.id}`}
                  >
                    Delete
                  </button>
                </div>
              </td>
            </tr>
          ))}
          {stakeholders.length === 0 && (
            <tr data-cy="stakeholder-empty-row">
              <td
                colSpan={7}
                className="p-6 text-center text-gray-500 text-sm"
                data-cy="stakeholder-empty-message"
              >
                No stakeholders found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default StakeholderList;
