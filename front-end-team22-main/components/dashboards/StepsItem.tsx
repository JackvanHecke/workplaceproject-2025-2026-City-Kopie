import React from "react";
import { Checklists } from "@types";

interface StepItemProps {
    item: Checklists;
    isDone: boolean;
    isActive: boolean;
    onClick: () => void;
}

const StepItem: React.FC<StepItemProps> = ({ item, isDone, isActive, onClick }) => (
    <li
        className={`flex items-start group cursor-pointer transition-all relative pl-1 ${isActive ? "font-medium" : ""
            }`}
        onClick={onClick}
    >
        <div className="absolute left-[-24px] top-1 shrink-0">
            {isDone ? (
                <svg className="h-5 w-5 text-[#0f8d97] drop-shadow-sm" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 14.586l7.293-7.293a1 1 0 011.414 0z" clipRule="evenodd" />
                </svg>
            ) : (
                <div className={`h-4 w-4 rounded-full border-2 mt-0.5 transition-colors ${isActive ? "border-teal-500 bg-teal-50" : "border-gray-300 group-hover:border-teal-400"
                    }`} />
            )}
        </div>
        <span className={`text-[15px] leading-snug transition-colors ${isDone ? "text-gray-500 line-through decoration-gray-400" : isActive ? "text-teal-800" : "text-teal-900 font-medium"
            }`}>
            {item.name}
        </span>
    </li>
);

export default StepItem;