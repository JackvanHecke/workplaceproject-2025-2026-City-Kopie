"use client";

import React, { useMemo, useState } from "react";
import { Coach, CoachOfferDTO, CoachAvailabilityDTO } from "../../types";
import OfferDetailsModal from "./OfferDetailsModal";

type Props = {
  coach: Coach;
  location: any;
  onEdit: () => void;
  onDelete: () => void;
};

const formatTime = (iso?: string) => {
  if (!iso) return "-";
  try {
    const d = new Date(iso);
    return d.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  } catch {
    return iso;
  }
};

const formatDate = (iso?: string) => {
  if (!iso) return "-";
  try {
    const d = new Date(iso);
    return d.toLocaleDateString();
  } catch {
    return iso;
  }
};

const formatAvailabilitySingle = (a?: CoachAvailabilityDTO) => {
  if (!a || (!a.availableFrom && !a.availableTo)) return "-";
  const from = formatTime(a.availableFrom);
  const to = formatTime(a.availableTo);
  const date = formatDate(a.availableFrom ?? a.availableTo);
  if (from && to && date) return `${from} → ${to}, ${date}`;
  if (from && date) return `${from}, ${date}`;
  if (to && date) return `${to}, ${date}`;
  return "-";
};

const firstOfferLabel = (offers?: CoachOfferDTO[]) => {
  if (!offers || offers.length === 0) return "-";
  const o = offers[0];
  return o.offerType ?? o.description ?? "Offer";
};

const priceLabel = (offers?: CoachOfferDTO[], coach?: Coach) => {
  if (coach?.isFree) return "Free";
  if (coach?.price && typeof coach.price.amount === "number")
    return `€ ${coach.price.amount}`;
  if (offers && offers.length > 0) {
    const p = offers[0].price;
    if (p === undefined || p === null) return "-";
    return offers[0].freeOrPaid === "free" ? "Free" : `€ ${p}`;
  }
  return "-";
};

const recurrenceLabel = (offers?: CoachOfferDTO[]) => {
  if (!offers || offers.length === 0) return "-";
  const oWithRec = offers.find(
    (o) => o.recurrence && o.recurrence.trim().length > 0
  );
  const o = oWithRec ?? offers[0];
  return o.recurrence ?? "-";
};

const CoachLocationRow: React.FC<Props> = ({
  coach,
  location,
  onEdit,
  onDelete,
}) => {
  const [offerOpen, setOfferOpen] = useState<boolean>(false);
  const [selectedOffer, setSelectedOffer] = useState<CoachOfferDTO | null>(
    null
  );

  const locationName = useMemo(() => {
    if (!location) return "-";
    return (location as any).benchName ?? (location as any).name ?? "-";
  }, [location]);

  const locationCity = useMemo(() => {
    if (!location) return "-";
    return (location as any).benchCity ?? (location as any).city ?? "-";
  }, [location]);

  const availabilityLabel = useMemo(() => {
    if (!coach.availability || coach.availability.length === 0) return "-";
    return formatAvailabilitySingle(coach.availability[0]);
  }, [coach.availability]);

  const openOffer = (offer?: CoachOfferDTO) => {
    if (!offer && coach.offers && coach.offers.length > 0) {
      setSelectedOffer(coach.offers[0]);
    } else {
      setSelectedOffer(offer ?? null);
    }
    setOfferOpen(true);
  };

  const id = coach.id ?? "unknown";

  return (
    <>
      <tr data-cy={`coach-row-${id}`}>
        <td
          className="px-6 py-4 whitespace-nowrap"
          data-cy={`coach-name-${id}`}
        >
          {coach.name ?? "-"}
        </td>
        <td className="px-6 py-4 whitespace-nowrap">{coach.role ?? "-"}</td>
        <td className="px-6 py-4 whitespace-nowrap">
          <button
            onClick={() => openOffer()}
            className="text-left underline text-blue-600 hover:text-blue-800"
            title="Open offer details"
            data-cy={`coach-offer-button-${id}`}
          >
            {firstOfferLabel(coach.offers)}
          </button>
        </td>
        <td className="px-6 py-4 whitespace-nowrap">
          {priceLabel(coach.offers, coach)}
        </td>
        <td className="px-6 py-4 whitespace-nowrap">{locationName}</td>
        <td className="px-6 py-4 whitespace-nowrap">{locationCity}</td>
        <td className="px-6 py-4 whitespace-nowrap">{availabilityLabel}</td>
        <td className="px-6 py-4 whitespace-nowrap">
          {recurrenceLabel(coach.offers)}
        </td>
        <td className="px-6 py-4 whitespace-nowrap">
          <div className="flex gap-2">
            <button
              onClick={onEdit}
              className="px-3 py-1 border rounded text-sm bg-white hover:bg-gray-50"
              data-cy={`coach-edit-${id}`}
            >
              Edit
            </button>
            <button
              onClick={onDelete}
              className="px-3 py-1 border rounded text-sm bg-white hover:bg-gray-50"
              data-cy={`coach-delete-${id}`}
            >
              Delete
            </button>
          </div>
        </td>
      </tr>

      {offerOpen && selectedOffer && (
        <OfferDetailsModal
          offer={selectedOffer}
          onClose={() => {
            setOfferOpen(false);
            setSelectedOffer(null);
          }}
          data-cy="offer-details-modal"
        />
      )}
    </>
  );
};

export default CoachLocationRow;
