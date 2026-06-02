import React from "react";
import {
  CommunicationCategory,
  DeliveryChannel,
  ProfileRoleEnum,
} from "../../types";

const CATEGORY_LABELS: Record<CommunicationCategory, string> = {
  INAUGURATION: "Inhuldiging",
  ACTIVITY: "Activiteit",
  COURSE_SERIES: "Reeks lessen",
  CHALLENGE: "Challenge",
  OTHER: "Andere",
};

const CHANNEL_LABELS: Record<DeliveryChannel, string> = {
  APP_POPUP: "Popup in de app (algemeen)",
  PROFILE_MESSAGE: "Bericht in profiel / inbox",
  DEVICE_POPUP: "Toestel-popup",
  PLATFORM_POPUP: "Platform-popup bij inloggen",
  MOBILE_POPUP: "Mobiele app notificatie",
};

type Props = {
  title: string;
  body: string;
  category: CommunicationCategory;
  channels: DeliveryChannel[];
  active: boolean;
  onChangeTitle: (v: string) => void;
  onChangeBody: (v: string) => void;
  onChangeCategory: (v: CommunicationCategory) => void;
  onToggleChannel: (ch: DeliveryChannel) => void;
  onChangeActive: (v: boolean) => void;
};

const CATEGORY_OPTIONS: CommunicationCategory[] = [
  "INAUGURATION",
  "ACTIVITY",
  "COURSE_SERIES",
  "CHALLENGE",
  "OTHER",
];

const CHANNEL_OPTIONS: DeliveryChannel[] = [
  "APP_POPUP",
  "PROFILE_MESSAGE",
  "DEVICE_POPUP",
  "PLATFORM_POPUP",
  "MOBILE_POPUP",
];

const CommunicationBasicsSection: React.FC<Props> = ({
  title,
  body,
  category,
  channels,
  active,
  onChangeTitle,
  onChangeBody,
  onChangeCategory,
  onToggleChannel,
  onChangeActive,
}) => {
  return (
    <>
      {/* Title */}
      <div>
        <label className="block font-medium mb-1">Titel</label>
        <input
          type="text"
          data-cy="communication-title-input"
          className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={title}
          onChange={(e) => onChangeTitle(e.target.value)}
          required
        />
      </div>

      {/* Body */}
      <div>
        <label className="block font-medium mb-1">Bericht</label>
        <textarea
          data-cy="communication-body-textarea"
          className="border rounded px-3 py-2 w-full min-h-[120px] focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={body}
          onChange={(e) => onChangeBody(e.target.value)}
          required
        />
      </div>

      {/* Category + Active */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block font-medium mb-1">Categorie</label>
          <select
            data-cy="communication-category-select"
            className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={category}
            onChange={(e) =>
              onChangeCategory(e.target.value as CommunicationCategory)
            }
          >
            {CATEGORY_OPTIONS.map((cat) => (
              <option key={cat} value={cat}>
                {CATEGORY_LABELS[cat]}
              </option>
            ))}
          </select>
        </div>

        <div className="flex items-center mt-6 md:mt-0">
          <label className="flex items-center gap-2">
            <input
              type="checkbox"
              data-cy="communication-active-checkbox"
              checked={active}
              onChange={(e) => onChangeActive(e.target.checked)}
            />
            <span>Actief</span>
          </label>
        </div>
      </div>

      {/* Channels */}
      <div>
        <label className="block font-medium mb-1">Kanalen</label>
        <div className="flex flex-wrap gap-4">
          {CHANNEL_OPTIONS.map((ch) => (
            <label
              key={ch}
              className="flex items-center gap-2 text-sm"
            >
              <input
                type="checkbox"
                data-cy={`communication-channel-${ch}`}
                checked={channels.includes(ch)}
                onChange={() => onToggleChannel(ch)}
              />
              <span>{CHANNEL_LABELS[ch]}</span>
            </label>
          ))}
        </div>
      </div>
    </>
  );
};

export default CommunicationBasicsSection;
