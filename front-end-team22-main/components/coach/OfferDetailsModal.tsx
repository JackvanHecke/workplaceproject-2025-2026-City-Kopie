"use client";

import React from "react";
import { CoachOfferDTO } from "../../types";

type Props = {
  offer: CoachOfferDTO;
  onClose: () => void;
};

const fmtDate = (iso?: string) => {
  if (!iso) return "-";
  try {
    return new Date(iso).toLocaleString();
  } catch {
    return iso;
  }
};

const OfferDetailsModal: React.FC<Props> = ({ offer, onClose }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4" data-cy="offer-details-modal">
      <div className="w-full max-w-xl rounded bg-white p-6 shadow-lg">
        <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-semibold" data-cy="offer-modal-title">{offer.offerType ?? "Offer details"}</h2>
            <button onClick={onClose} className="text-gray-600 hover:text-gray-800" data-cy="offer-modal-close">✕</button>
        </div>

        <div className="space-y-3">
          <div>
            <strong>Type:</strong> {offer.offerType ?? "-"}
          </div>
          <div>
            <strong>Target group:</strong> {offer.targetGroup ?? "-"}
          </div>
          <div>
            <strong>Description:</strong>
            <div className="mt-1 whitespace-pre-wrap">{offer.description ?? "-"}</div>
          </div>
          <div>
            <strong>When:</strong> {fmtDate(offer.startDatetime)} — {fmtDate(offer.endDatetime)}
          </div>
          <div>
            <strong>Recurrence:</strong> {offer.recurrence ?? "-"}
          </div>
          <div>
            <strong>Price:</strong>{" "}
            {offer.freeOrPaid === "free" ? "Free" : offer.price !== undefined ? `€ ${offer.price}` : "-"}
          </div>
        </div>

        <div className="flex justify-end gap-3 pt-4">
          <button onClick={onClose} className="px-4 py-2 rounded border bg-white hover:bg-gray-50" data-cy="offer-modal-close-2">Close</button>
        </div>
      </div>
    </div>
  );
};

export default OfferDetailsModal;
