import React from "react";
import { Profile } from "../../types";

type Props = {
  allProfiles: Profile[];
  profilesLoading: boolean;
  profileSearch: string;
  selectedRecipients: Profile[];
  createdByProfileId: number;
  createdByName?: string;

  onChangeProfileSearch: (v: string) => void;
  onAddRecipient: (p: Profile) => void;
  onRemoveRecipient: (id?: number) => void;
};

const CommunicationRecipientsSection: React.FC<Props> = ({
  allProfiles,
  profilesLoading,
  profileSearch,
  selectedRecipients,
  createdByProfileId,
  createdByName,
  onChangeProfileSearch,
  onAddRecipient,
  onRemoveRecipient,
}) => {
  const term = profileSearch.toLowerCase();
  const suggestions =
    term.length === 0
      ? []
      : allProfiles
          .filter((p) => {
            const email = (p.email || "").toLowerCase();
            const name = (p.name || "").toLowerCase();
            return email.includes(term) || name.includes(term);
          })
          .filter(
            (p) => !selectedRecipients.some((sel) => sel.id === p.id)
          )
          .slice(0, 5);

  return (
    <>
      {/* Explicit recipients via e-mail search */}
      <div>
        <label className="block font-medium mb-1">
          Specifieke profielen (via e-mailadres)
        </label>
        <div className="relative">
          <input
            type="text"
            data-cy="recipients-search-input"
            className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Zoek op e-mailadres of naam..."
            value={profileSearch}
            onChange={(e) => onChangeProfileSearch(e.target.value)}
          />
          {profilesLoading && (
            <div
              className="absolute right-3 top-2 text-xs text-gray-400"
              data-cy="recipients-loading-indicator"
            >
              laden...
            </div>
          )}

          {suggestions.length > 0 && (
            <ul
              className="absolute z-20 mt-1 w-full bg-white border border-gray-200 rounded shadow-lg max-h-60 overflow-y-auto"
              data-cy="recipients-suggestions-list"
            >
              {suggestions.map((p) => (
                <li
                  key={p.id}
                  data-cy={`recipients-suggestion-${p.id}`}
                  className="px-3 py-2 text-sm hover:bg-gray-100 cursor-pointer"
                  onClick={() => onAddRecipient(p)}
                >
                  <div className="font-medium">
                    {p.name || "Onbekende naam"}
                  </div>
                  <div className="text-xs text-gray-500">
                    {p.email || "Geen e-mail beschikbaar"}
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>

        {selectedRecipients.length > 0 && (
          <div
            className="mt-2 flex flex-wrap gap-2"
            data-cy="recipients-selected-list"
          >
            {selectedRecipients.map((p) => (
              <span
                key={p.id}
                data-cy={`recipient-chip-${p.id}`}
                className="inline-flex items-center gap-1 px-3 py-1 rounded-full bg-blue-100 text-blue-800 text-xs"
              >
                {p.name || p.email || `Profiel ${p.id}`}
                <button
                  type="button"
                  data-cy={`recipient-remove-${p.id}`}
                  className="ml-1 text-blue-700 hover:text-blue-900"
                  onClick={() => onRemoveRecipient(p.id)}
                >
                  ×
                </button>
              </span>
            ))}
          </div>
        )}
      </div>

      {/* Creator (readonly info) */}
      <div
        className="border-t pt-3 mt-4 flex justify-between items-center text-sm text-gray-600"
        data-cy="creator-info"
      >
        <div>
          <span className="font-medium">Aangemaakt door: </span>
          <span data-cy="creator-name">
            {createdByName || `Profiel #${createdByProfileId}`}
          </span>
        </div>
      </div>
    </>
  );
};

export default CommunicationRecipientsSection;
