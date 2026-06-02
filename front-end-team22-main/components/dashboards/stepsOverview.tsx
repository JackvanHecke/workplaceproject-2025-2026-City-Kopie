"use client";
import React, { useMemo, useState, useEffect, useCallback } from "react";
import ChecklistsService from "@services/ChecklistsService";
import { Phase, Checklists, Location } from "@types";

import ItemDetailPanel from "./ItemDetailPanel";
import ProgressHeader from "./PorgressHeader";
import StepItem from "./StepsItem";

type Props = {
  location?: Location;
  phases?: Phase[];
  onToggleComplete: () => Promise<void>;
};

// De exacte volgorde gebaseerd op jouw afbeelding
const CHECKLIST_ORDER = [
  // Voorbereiding
  "Demo- of testfase",
  "Goedkeuring samenwerking",
  "Aanwijzing trekker(s)",
  "Trekker volgt workshop",
  "Trekker contacteert stakeholders",
  // Opstart
  "Kick-off met stakeholders",
  "Locatiebepaling beweegtoestel",
  "Plaatsing beweegbank",
  "Plaatsing beweegtoestel",
  "Beweegtoestel toevoegen in app",
  "Activatoren volgen trainersopleiding",
  // Uitvoering
  "Officiële ingebruikname",
  "Aanbod selecteren en plannen",
  "Planning aanbod",
  "Communicatie",
  "Aanbod uitrollen",
  "Uitrol activiteitenaanbod",
  // Opvolging
  "Evaluatieoverleg trekker, stakeholders en activatoren",
  "Evaluatieoverleg trekker en IPitup",
  "Onderhoud en veiligheidsinspectie",
];

const sectionIcons: Record<string, string> = {
  Voorbereiding: "/images/Voorbereiding1.png",
  Opstart: "/images/Opstart.png",
  Uitvoering: "/images/Uitvoering.png",
  Opvolging: "/images/Opvolging.png",
};

const offsets = ["lg:-mt-[50px]", "lg:mt-[300px]", "lg:mt-[225px]", "lg:mt-[80px]"];

const StepsOverview: React.FC<Props> = ({ location, phases, onToggleComplete }) => {
  const [completedIds, setCompletedIds] = useState<Set<number>>(new Set());
  const [activeItem, setActiveItem] = useState<{
    phaseIdx: number;
    itemIdx: number;
    item: Checklists;
  } | null>(null);

  const stepsData = useMemo(() => {
    if (!phases) return [];

    const phaseOrder = ["Voorbereiding", "Opstart", "Uitvoering", "Opvolging"];
    
    return [...phases]
      .filter((p) => p.id && p.name)
      .sort((a, b) => phaseOrder.indexOf(a.name!) - phaseOrder.indexOf(b.name!));
  }, [phases]);

  useEffect(() => {
    if (location?.completedChecklistItems) {
      setCompletedIds(new Set(location.completedChecklistItems.map((c) => c.id.checklistId)));
    }
  }, [location]);

  const toggleItem = useCallback(async () => {
    if (!activeItem || !location?.id || !activeItem.item.id) return;
    const itemId = activeItem.item.id;
    const isCurrentlyDone = completedIds.has(itemId);

    setCompletedIds((prev) => {
      const next = new Set(prev);
      isCurrentlyDone ? next.delete(itemId) : next.add(itemId);
      return next;
    });

    try {
      const response = await ChecklistsService.markChecklistAsComplete(String(itemId), String(location.id));
      if (response.ok) await onToggleComplete();
      else throw new Error();
    } catch {
      setCompletedIds((prev) => {
        const next = new Set(prev);
        isCurrentlyDone ? next.add(itemId) : next.delete(itemId);
        return next;
      });
    }
  }, [activeItem, location?.id, completedIds, onToggleComplete]);

  const stats = useMemo(() => {
    const total = stepsData.reduce((acc, p) => acc + (p.checklists?.length || 0), 0);
    const progress = total ? Math.round((completedIds.size / total) * 100) : 0;
    return { total, progress, completed: completedIds.size };
  }, [stepsData, completedIds]);

  if (!phases || !location) return <div className="p-12 text-center text-teal-600">Laden...</div>;

  return (
    <section className="space-y-4">
      <ProgressHeader
        locationStreet={location.street}
        progress={stats.progress}
        completedCount={stats.completed}
        totalCount={stats.total}
      />

      <div className="relative w-full py-10 mb-8">
        <div className="absolute inset-0 flex items-start justify-center pointer-events-none -z-10 pt-8">
          <img src="/images/pijl.png" alt="flow" className="w-full max-w-7xl opacity-90" />
        </div>

        <div className="flex flex-col lg:flex-row items-start justify-around px-4 lg:px-0 relative z-10">
          {stepsData.map((phase, sIdx) => (
            <div
              key={phase.id}
              className={`flex-1 min-w-[220px] max-w-[280px] flex flex-col items-center mb-16 lg:mb-0 ${offsets[sIdx] || ""}`}
            >
              <div className="bg-[#0f8d97] rounded-full p-4 w-28 h-28 flex items-center justify-center shadow-lg mb-4">
                <img
                  src={sectionIcons[phase.name!] || "/images/default-box.png"}
                  className="w-14 h-14 invert brightness-0"
                  alt="icon"
                />
              </div>
              <h2 className="text-2xl font-extrabold text-teal-900 mb-6">{phase.name}</h2>
              <ul className="space-y-3 w-full px-2">
                {(phase.checklists || [])
                  .slice()
                  .sort((a, b) => {
                    const indexA = CHECKLIST_ORDER.indexOf(a.name!);
                    const indexB = CHECKLIST_ORDER.indexOf(b.name!);
                    return (indexA === -1 ? 99 : indexA) - (indexB === -1 ? 99 : indexB);
                  })
                  .map((item, iIdx) => (
                    <StepItem
                      key={item.id}
                      item={item}
                      isDone={completedIds.has(item.id!)}
                      isActive={activeItem?.item.id === item.id}
                      onClick={() => setActiveItem({ phaseIdx: sIdx, itemIdx: iIdx, item })}
                    />
                  ))}
              </ul>
            </div>
          ))}
        </div>
      </div>

      <div
        className="transition-all duration-500 ease-in-out overflow-hidden"
        style={{ maxHeight: activeItem ? "800px" : "0px", opacity: activeItem ? 1 : 0 }}
      >
        <ItemDetailPanel
          item={activeItem?.item || null}
          isChecked={activeItem?.item.id ? completedIds.has(activeItem.item.id) : false}
          onToggle={toggleItem}
          onClose={() => setActiveItem(null)}
        />
      </div>
    </section>
  );
};

export default StepsOverview;