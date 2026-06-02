import React, { useEffect, useState } from "react";
import { Stakeholder } from "@types";

type Props = {
  initial?: Stakeholder;
  onSave: (s: Stakeholder) => void;
  onCancel?: () => void;
};

const StakeholderForm: React.FC<Props> = ({ initial, onSave, onCancel }) => {
  const [organisation, setOrganisation] = useState(initial?.organisation || "");
  const [contactPerson, setContactPerson] = useState(
    initial?.contactPerson || ""
  );
  const [email, setEmail] = useState(initial?.email || "");
  const [phoneNumber, setPhoneNumber] = useState(initial?.phoneNumber || "");
  const [role, setRole] = useState(initial?.role || "");

  useEffect(() => {
    if (initial) {
      setOrganisation(initial.organisation);
      setContactPerson(initial.contactPerson);
      setEmail(initial.email);
      setPhoneNumber(initial.phoneNumber);
      setRole(initial.role);
    }
  }, [initial]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const newStakeholder: Stakeholder & { id?: number } = {
      organisation,
      contactPerson,
      email,
      phoneNumber,
      role,
      partnershipDecided: initial?.partnershipDecided ?? false,
      id: initial?.id,
    };
    onSave(newStakeholder);
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="space-y-2 p-4 border rounded-md bg-white"
      data-cy="stakeholder-form"
    >
      <div>
        <label className="block text-sm font-medium">Organisation</label>
        <input
          required
          value={organisation}
          onChange={(e) => setOrganisation(e.target.value)}
          className="w-full border p-2 rounded"
          data-cy="stakeholder-organisation-input"
        />
      </div>
      <div>
        <label className="block text-sm font-medium">Contact Person</label>
        <input
          required
          value={contactPerson}
          onChange={(e) => setContactPerson(e.target.value)}
          className="w-full border p-2 rounded"
          data-cy="stakeholder-contact-input"
        />
      </div>
      <div className="grid grid-cols-2 gap-2">
        <div>
          <label className="block text-sm font-medium">Email</label>
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="w-full border p-2 rounded"
            data-cy="stakeholder-email-input"
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Phone Number</label>
          <input
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="w-full border p-2 rounded"
            data-cy="stakeholder-phone-input"
          />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium">Role</label>
        <input
          value={role}
          onChange={(e) => setRole(e.target.value)}
          className="w-full border p-2 rounded"
          data-cy="stakeholder-role-input"
        />
      </div>

      <div className="flex gap-2 justify-end">
        {onCancel && (
          <button
            type="button"
            onClick={onCancel}
            className="px-3 py-1 border rounded"
            data-cy="stakeholder-cancel-button"
          >
            Cancel
          </button>
        )}
        <button
          type="submit"
          className="px-3 py-1 bg-blue-600 text-white rounded"
          data-cy="stakeholder-save-button"
        >
          Save
        </button>
      </div>
    </form>
  );
};

export default StakeholderForm;
