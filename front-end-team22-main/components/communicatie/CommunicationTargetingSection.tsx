// components/communicatie/CommunicationTargetingSection.tsx
import React from "react";
import { ProfileRoleEnum } from "../../types";

type Props = {
  minAge?: number;
  maxAge?: number;
  targetRoles: ProfileRoleEnum[];

  onChangeMinAge: (v: number | undefined) => void;
  onChangeMaxAge: (v: number | undefined) => void;
  onToggleRole: (role: ProfileRoleEnum) => void;
};

const ROLE_LABELS: Record<ProfileRoleEnum, string> = {
  END_USER: "Eindgebruiker",
  COACH: "Coach",
  CLIENT: "Klant",
  ADMIN: "Beheerder",
};

const CommunicationTargetingSection: React.FC<Props> = ({
  minAge,
  maxAge,
  targetRoles,
  onChangeMinAge,
  onChangeMaxAge,
  onToggleRole,
}) => {
  return (
    <section
      className="rounded-xl border border-gray-200 bg-gray-50 p-4 space-y-4"
      data-cy="targeting-section"
    >
      {/* Leeftijd */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium mb-1">
            Minimumleeftijd
          </label>
          <input
            type="number"
            data-cy="targeting-min-age-input"
            className="border rounded px-2 py-1 w-full"
            value={minAge ?? ""}
            onChange={(e) =>
              onChangeMinAge(
                e.target.value === "" ? undefined : Number(e.target.value)
              )
            }
          />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">
            Maximumleeftijd
          </label>
          <input
            type="number"
            data-cy="targeting-max-age-input"
            className="border rounded px-2 py-1 w-full"
            value={maxAge ?? ""}
            onChange={(e) =>
              onChangeMaxAge(
                e.target.value === "" ? undefined : Number(e.target.value)
              )
            }
          />
        </div>
      </div>

      {/* Rollen */}
      <div>
        <label className="block text-sm font-medium mb-1">Rollen</label>
        <div className="flex flex-wrap gap-4" data-cy="targeting-roles-container">
          {Object.keys(ROLE_LABELS).map((key) => {
            const role = key as ProfileRoleEnum;
            return (
              <label
                key={role}
                className="flex items-center gap-1 text-sm"
              >
                <input
                  type="checkbox"
                  data-cy={`targeting-role-${role}`}
                  checked={targetRoles.includes(role)}
                  onChange={() => onToggleRole(role)}
                />
                <span>{ROLE_LABELS[role]}</span>
              </label>
            );
          })}
        </div>
        <p className="text-xs text-gray-500 mt-1">
          Laat leeg om alle rollen toe te laten.
        </p>
      </div>
    </section>
  );
};

export default CommunicationTargetingSection;
