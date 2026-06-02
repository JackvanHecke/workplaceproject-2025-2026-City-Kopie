import Head from "next/head";
import Header from "@components/header";
import SideNav from "@components/sideNav";
import { useEffect, useState } from "react";
import { Workshop } from "@types";
import WorkshopService from "@services/WorkshopService";
import WorkshopList from "@components/workshops/WorkshopList";

const Activiteiten: React.FC = () => {
  const [workshops, setWorkshops] = useState<Workshop[]>([]);
  const [filtered, setFiltered] = useState<Workshop[]>([]);
  const [query, setQuery] = useState("");
  const [error, setError] = useState<string | null>(null);

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
    setFiltered(workshops.filter((w) => w.naam.toLowerCase().includes(lower)));
  };

  useEffect(() => {
    fetchWorkshops();
  }, []);

  return (
    <>
      <Head>
        <title>Activiteiten</title>
        <meta name="description" content="Exam app" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <Header />

      <div className="flex">
        <SideNav />

        <main className="text-center md:mt-24 mx-auto md:w-3/5 lg:w-1/2 p-4">
          <h1 className="text-2xl font-bold mb-4">Activiteiten – Workshops</h1>

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
      </div>
    </>
  );
};

export default Activiteiten;
