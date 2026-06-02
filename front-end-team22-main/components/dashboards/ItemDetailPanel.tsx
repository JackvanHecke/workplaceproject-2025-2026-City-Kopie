import React from "react";
import { Checklists } from "@types";

interface ItemDetailPanelProps {
    item: Checklists | null;
    isChecked: boolean;
    onToggle: () => void;
    onClose: () => void;
}

const ItemDetailPanel: React.FC<ItemDetailPanelProps> = ({ item, isChecked, onToggle, onClose }) => {
    if (!item) return null;

    return (
        <div className="mt-8 p-8 bg-teal-50/80 rounded-[2rem] border border-teal-200 shadow-inner relative">
            <button onClick={onClose} className="absolute top-4 right-4 text-teal-600 hover:text-teal-800">
                <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
            </button>

            <div className="flex items-start">
                <div className="mr-6 mt-1 shrink-0">
                    <input
                        type="checkbox"
                        checked={isChecked}
                        onChange={onToggle}
                        className="h-8 w-8 rounded-md border-2 border-teal-400 text-[#0f8d97] focus:ring-2 focus:ring-teal-500 focus:ring-offset-2 transition cursor-pointer"
                    />
                </div>
                <div>
                    <h4 className="text-2xl font-bold text-teal-900 mb-4">{item.name}</h4>
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-teal-100">
                        <p className="text-base text-gray-700 whitespace-pre-wrap leading-relaxed">
                            {item.info || "Geen extra informatie beschikbaar."}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ItemDetailPanel;