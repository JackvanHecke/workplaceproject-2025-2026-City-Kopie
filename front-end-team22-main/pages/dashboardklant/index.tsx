import React, { useCallback, useEffect, useState } from "react";
import StickyNotes from "@components/dashboards/addStickyNotes";
import LocationService from "@services/LocationService"; 
import Header from "@components/header";
import SideNav from "@components/sideNav";
import StepsOverview from "@components/dashboards/stepsOverview";
import PhaseService from "@services/PhaseService";
import { Phase, Location } from "@types";

const StappenplanPage: React.FC = () => {
  const [location, setLocation] = useState<Location | null>(null);
  const [phases, setPhases] = useState<Phase[]>([]);

  const fetchPhases = useCallback(async () => {
    try {
      const phaseRes = await PhaseService.getAll();
      const phaseData = await phaseRes.json();
      setPhases(phaseData);
    } catch (error) {
      console.error("Error fetching phases:", error);
    }
  }, []);

  const fetchLocation = useCallback(async (id: number) => {
    try {
      const res = await LocationService.getById(id);
      setLocation(res);
    } catch (e) {
      console.error("Failed to refresh location", e);
    }
  }, []);

  const handleRefreshData = async () => {
    if (location?.id) {
      await fetchLocation(location.id);
    }
    await fetchPhases();
  };

  useEffect(() => {
    const storedLocationStr = localStorage.getItem("selectedLocation");
    if (storedLocationStr) {
      const parsedLoc = JSON.parse(storedLocationStr);
      setLocation(parsedLoc);
      if (parsedLoc.id) fetchLocation(parsedLoc.id);
    }

    fetchPhases();
    console.log(phases);
  }, [fetchPhases, fetchLocation]);

  if (!location) return <div>No location selected...</div>;

  return (
    <>
      <Header />
      <div className="flex">
        <SideNav />
        <main className="p-4 md:w-4/5 mx-auto space-y-12">
          <StepsOverview
            location={location}
            phases={phases}
            onToggleComplete={handleRefreshData}
          />
          {location && <StickyNotes locationId={location.id} />}
        </main>
      </div>
    </>
  );
};

export default StappenplanPage;
