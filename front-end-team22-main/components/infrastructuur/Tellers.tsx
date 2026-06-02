import React from "react";
import type { Counters } from "../../services/StatsService";

type Props = { counters: Counters | null };

export default function Tellers({ counters }: Props) {
  return (
    <section className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-8">
      <div className="rounded-2xl bg-white shadow-md p-5 text-center">
        <div className="text-sm text-gray-500">Totaal aantal banken</div>
        <div className="text-4xl font-extrabold leading-tight">
          {counters?.total_benches ?? "—"}
        </div>
      </div>

      <div className="rounded-2xl bg-white shadow-md p-5 text-center">
        <div className="text-sm text-gray-500">Aantal verschillende landen</div>
        <div className="text-4xl font-extrabold leading-tight">
          {counters?.distinct_countries ?? "—"}
        </div>
      </div>
    </section>
  );
}
