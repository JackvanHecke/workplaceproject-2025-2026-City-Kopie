export type Counters = {
  total_benches: number;
  distinct_countries: number;
  benches_last_7_days: number;
};

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export async function getCounters(): Promise<Counters> {
  const res = await fetch(`${API_URL}/api/counters`);
  if (!res.ok) throw new Error(await res.text());

  const data = await res.json();
  return {
    total_benches: data.totalBenches,
    distinct_countries: data.distinctCountries,
    benches_last_7_days: data.benchesLast7Days,
  };
}
