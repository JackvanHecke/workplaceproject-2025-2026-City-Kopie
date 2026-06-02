import WorkshopService from "@services/WorkshopService";
import Header from "@components/header";
import Head from "next/head";
import { useEffect, useState } from "react";
import { Workshop } from "@types";
import WorkshopList from "@components/workshops/WorkshopList";

const WorkshopsPage = () => {
  const [workshops, setWorkshops] = useState<Workshop[]>([]);
  const [filtered, setFiltered] = useState<Workshop[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [query, setQuery] = useState("");

  const fetchWorkshops = async () => {
    try {
      const data = await WorkshopService.getAllWorkshops();
      setWorkshops(data);
      setFiltered(data);
    } catch (err: any) {
      setError(err.message);
    }
  };

  const filterWorkshops = (value: string) => {
    setQuery(value);
    const lower = value.toLowerCase();
    const result = workshops.filter((w) =>
      w.naam.toLowerCase().includes(lower)
    );
    setFiltered(result);
  };

  useEffect(() => {
    fetchWorkshops();
  }, []);

  return (
    <>
      <Head>
        <title>Workshops</title>
        <meta name="description" content="List of Workshops" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Header />
      <main className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Workshops</h1>

        {/* Filter Input */}
        <input
          type="text"
          value={query}
          onChange={(e) => filterWorkshops(e.target.value)}
          placeholder="Filter workshops..."
          className="border p-2 mb-4 w-full"
        />

        {error ? (
          <div className="text-red-500">Error: {error}</div>
        ) : (
          <WorkshopList workshops={filtered} />
        )}
      </main>
    </>
  );
};

export default WorkshopsPage;
