import React from "react";
import { Coach } from "../../types";
import CoachLocationRow from "./CoachLocationRow";

type Props = {
  coaches: Coach[];
  onEdit: (c: Coach) => void;
  onDelete: (id?: number) => void;
};

const CoachesTable: React.FC<Props> = ({ coaches, onEdit, onDelete }) => {
  const coachLocationPairs = React.useMemo(() => {
    if (!coaches) return [];
    return coaches.flatMap((c) => {
      if (!c.locations || c.locations.length === 0) {
        return [{ coach: c, location: undefined }];
      }
      return c.locations.map((loc) => ({ coach: c, location: loc }));
    });
  }, [coaches]);

  return (
    <div className="overflow-x-auto rounded border">
      <table className="min-w-full divide-y" data-cy="coaches-table">
        <thead className="bg-gray-50">
          <tr>
            <th className="px-6 py-3 text-left text-sm font-medium">Name</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Role</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Offer</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Price</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Location/Bench</th>
            <th className="px-6 py-3 text-left text-sm font-medium">City</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Availability</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Recurrence</th>
            <th className="px-6 py-3 text-left text-sm font-medium">Actions</th>
          </tr>
        </thead>

        <tbody className="bg-white divide-y" data-cy="coaches-tbody">
          {coachLocationPairs && coachLocationPairs.length > 0 ? (
            coachLocationPairs.map((pair, idx) => {
              const key = `${pair.coach.id ?? "c"}-${(pair.location && (pair.location.id ?? idx)) ?? "noloc"}-${idx}`;
              return (
                <CoachLocationRow
                  key={key}
                  coach={pair.coach}
                  location={pair.location}
                  onEdit={() => onEdit(pair.coach)}
                  onDelete={() => onDelete(pair.coach.id)}
                />
              );
            })
          ) : (
            <tr>
              <td className="px-6 py-8 text-center" colSpan={9}>
                No coaches found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default CoachesTable;
