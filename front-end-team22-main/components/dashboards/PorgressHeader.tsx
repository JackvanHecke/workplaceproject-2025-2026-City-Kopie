import React from "react";

interface ProgressHeaderProps {
    locationStreet?: string;
    progress: number;
    completedCount: number;
    totalCount: number;
}


const ProgressHeader: React.FC<ProgressHeaderProps> = ({
    locationStreet,
    progress,
    completedCount,
    totalCount,
}) => (

    <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6 mb-12">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-3">
            <div>
                {locationStreet && (
                    <span className="text-xs font-bold text-teal-600 uppercase tracking-wider mb-1 block">
                        Locatie: {locationStreet}
                    </span>
                )}
                <h1 className="text-2xl font-extrabold uppercase text-teal-900">
                    Stappenplan
                </h1>
                <p className="text-sm text-gray-600">
                    Voor optimale activatie van de beweegtoestellen {locationStreet ? `bij ${locationStreet}` : ""}
                </p>
            </div>

            <div className="text-right flex flex-col items-end">
                <span className="text-3xl font-bold text-teal-600 block">{progress}%</span>
                <span className="text-xs text-gray-500">
                    {completedCount}/{totalCount} voltooid
                </span>
            </div>
        </div>

        <div className="w-full h-3 bg-gray-100 rounded-full overflow-hidden">
            <div
                className="h-full bg-gradient-to-r from-teal-500 to-[#0f8d97] transition-all duration-500 ease-out rounded-full"
                style={{ width: `${progress}%` }}
            />
        </div>
    </div>
);

export default ProgressHeader;