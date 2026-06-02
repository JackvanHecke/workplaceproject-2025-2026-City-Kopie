import React, { useState } from "react";
import { Coach } from "../../types";

type Props = {
  coach: Coach;
  onClose: () => void;
  onSave: (updated: Coach) => void;
};

const CoachEditModal: React.FC<Props> = ({ coach, onClose, onSave }) => {
 const [name, setName] = useState<string>(coach.name ?? "");
  const firstOffer = coach.offers && coach.offers.length > 0 ? coach.offers[0] : undefined;
  const [offerType, setOfferType] = useState<string | undefined>(firstOffer?.offerType);
  const [isFree, setIsFree] = useState<boolean>(firstOffer?.freeOrPaid === "free" || coach.isFree === true);
  const [priceAmount, setPriceAmount] = useState<number>(coach.price?.amount ?? (firstOffer?.price ?? 0));
  const [priceCurrency, setPriceCurrency] = useState<string>("EUR");

  const save = () => {
    const updated: Coach = { ...coach };
    updated.name = name;
    const newOffer = {
      ...(firstOffer || {}),
      offerType,
      freeOrPaid: isFree ? "free" : "paid",
      price: isFree ? 0 : Number(priceAmount || 0),
    };
    updated.offers = updated.offers && updated.offers.length > 0 ? [newOffer, ...updated.offers.slice(1)] : [newOffer];
    updated.isFree = isFree;
    updated.price = isFree ? { amount: 0 } : { amount: Number(priceAmount || 0) };
    onSave(updated);
  };

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/40 p-4" data-cy="coach-edit-modal">
      <div className="w-full max-w-2xl rounded bg-white p-6 shadow-lg">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">Edit Coach</h2>
          <button
            onClick={onClose}
            className="text-gray-600 hover:text-gray-800"
            aria-label="Close edit modal"
          >
            ✕
          </button>
        </div>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Name</label>
            <input
              className="mt-1 block w-full rounded border px-3 py-2"
              value={name}
              onChange={(e) => setName(e.target.value)}
              data-cy="coach-edit-name"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium">Offer Type</label>
              <input
                className="mt-1 block w-full rounded border px-3 py-2"
                value={offerType ?? ""}
                onChange={(e) => setOfferType(e.target.value)}
              />
            </div>

            <div>
              <label className="block text-sm font-medium">Is free?</label>
              <select
                value={isFree ? "yes" : "no"}
                onChange={(e) => setIsFree(e.target.value === "yes")}
                className="mt-1 block w-full rounded border px-3 py-2"
              >
                <option value="no">No</option>
                <option value="yes">Yes</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-3 gap-4 items-end">
            <div>
              <label className="block text-sm font-medium">Price amount</label>
              <input
                type="number"
                className="mt-1 block w-full rounded border px-3 py-2"
                value={priceAmount ?? 0}
                onChange={(e) => setPriceAmount(Number(e.target.value))}
                disabled={isFree}
              />
            </div>

            <div>
              <label className="block text-sm font-medium">Currency</label>
              <input
                className="mt-1 block w-full rounded border px-3 py-2"
                value={priceCurrency}
                onChange={(e) => setPriceCurrency(e.target.value)}
              />
            </div>

            <div className="flex justify-end gap-3 pt-2">
              <button
                onClick={onClose}
                className="px-4 py-2 rounded border bg-white hover:bg-gray-50"
                data-cy="coach-edit-cancel"
              >
                Cancel
              </button>

              <button
                onClick={save}
                className="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700"
                data-cy="coach-edit-save"   
              >
                Save
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CoachEditModal;
