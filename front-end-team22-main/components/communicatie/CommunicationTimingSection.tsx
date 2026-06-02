import React from "react";

type Props = {
  startDate: string;
  startTime: string;
  endDate: string;
  endTime: string;
  onChangeStartDate: (v: string) => void;
  onChangeStartTime: (v: string) => void;
  onChangeEndDate: (v: string) => void;
  onChangeEndTime: (v: string) => void;
};

const CommunicationTimingSection: React.FC<Props> = ({
  startDate,
  startTime,
  endDate,
  endTime,
  onChangeStartDate,
  onChangeStartTime,
  onChangeEndDate,
  onChangeEndTime,
}) => {
  return (
    <div
      className="grid grid-cols-1 md:grid-cols-2 gap-4"
      data-cy="timing-section"
    >
      <div>
        <label className="block font-medium mb-1">Startdatum</label>
        <input
          type="date"
          data-cy="timing-start-date-input"
          className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={startDate}
          onChange={(e) => onChangeStartDate(e.target.value)}
          required
        />
        <label className="block font-medium mb-1 mt-2">Starttijd</label>
        <input
          type="time"
          data-cy="timing-start-time-input"
          className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={startTime}
          onChange={(e) => onChangeStartTime(e.target.value)}
          required
        />
      </div>

      <div>
        <label className="block font-medium mb-1">Einddatum (optioneel)</label>
        <input
          type="date"
          data-cy="timing-end-date-input"
          className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={endDate}
          onChange={(e) => onChangeEndDate(e.target.value)}
        />
        <label className="block font-medium mb-1 mt-2">
          Eindtijd (optioneel)
        </label>
        <input
          type="time"
          data-cy="timing-end-time-input"
          className="border rounded px-3 py-2 w-full focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={endTime}
          onChange={(e) => onChangeEndTime(e.target.value)}
        />
      </div>
    </div>
  );
};

export default CommunicationTimingSection;
