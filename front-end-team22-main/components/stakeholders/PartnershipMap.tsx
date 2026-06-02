import React, { useMemo } from "react";
import type { PartnershipCategory } from "@types";

type PartnershipMapProps = {
  categories: PartnershipCategory[];
  decidedCategoryIds: Set<number>;
  onToggleCategory: (categoryId: number) => void;
};

type Bubble = {
  cx: number;
  cy: number;
  rx: number;
  ry: number;
  rotation: number;
};

// Outer oval (your existing geometry)
const OUTER = {
  cx: 560.1096534729004,
  cy: 443.53563690185547,
  rx: 460.65869140625,
  ry: 396.6849536895752,
  rotation: -14.235286245594281,
};

// Bubbles (your existing geometry)
const BUBBLES: Bubble[] = [
  { cx: 586.2938385009766, cy: 527.9746189117432, rx: 63.296463327731026, ry: 47.47234749579827, rotation: -20 },
  { cx: 715.9649200439453, cy: 394.6703281402588, rx: 78.18227972328643, ry: 63.967319773597985, rotation: -20 },
  { cx: 460.95521545410156, cy: 548.6873507499695, rx: 84.44846740118734, ry: 71.11449886415775, rotation: -20 },
  { cx: 341.4815216064453, cy: 271.88216400146484, rx: 96.19430719795417, ry: 76.15382653171372, rotation: -20 },
  { cx: 567.7506561279297, cy: 266.4697437286377, rx: 101.80377534860668, ry: 83.29399801249637, rotation: -20 },
  { cx: 879.4430809020996, cy: 400.93915939331055, rx: 89.35987939274725, ry: 70.74323785259156, rotation: -20 },
  { cx: 526.7157707214355, cy: 401.6866912841797, rx: 79.39182289312929, ry: 62.851859790394016, rotation: -20 },
  { cx: 633.1386833190918, cy: 138.7015609741211, rx: 96.42293858242031, ry: 81.19826406940659, rotation: -20.842838210406555 },
  { cx: 763.7926843166351, cy: 507.8534393310547, rx: 57.767789162892235, ry: 46.76440075091276, rotation: -20 },
  { cx: 453.0553550720215, cy: 169.60001754760742, rx: 96.54537835266369, ry: 79.75487776959174, rotation: -20 },
  { cx: 668.6228179931641, cy: 577.0252876281738, rx: 40, ry: 30, rotation: -20 },
  { cx: 792.1353912353516, cy: 234.3270149230957, rx: 91.79741684840643, ry: 78.02780432114545, rotation: -20 },
  { cx: 266.8567605018616, cy: 491.02424240112305, rx: 158.8467238719562, ry: 138.99088338796167, rotation: -15.042550909810686 },
];

const getLabelFontSize = (label: string) => {
  const len = label.length;
  if (len > 220) return 12;
  if (len > 160) return 13;
  if (len > 100) return 14;
  if (len > 60) return 15;
  return 17;
};

const PartnershipMap: React.FC<PartnershipMapProps> = ({
  categories,
  decidedCategoryIds,
  onToggleCategory,
}) => {
  // Only render as many categories as we have bubbles
  const items = useMemo(() => categories.slice(0, BUBBLES.length), [categories]);

  const total = BUBBLES.length;
  const completedCount = items.filter((c) => decidedCategoryIds.has(c.id)).length;
  const percentage = total > 0 ? Math.round((completedCount / total) * 100) : 0;

  return (
    <section className="mt-8 space-y-4" data-cy="partnership-map-section">
      <p className="text-gray-600 text-sm max-w-2xl" data-cy="partnership-map-description">
        Elke bubbel stelt een mogelijke partnergroep voor. Klik op een bubbel om aan te duiden dat
        de samenwerking beslist is.
      </p>

      {/* Progress bar */}
      <div className="w-full max-w-5xl mx-auto mb-2" data-cy="partnership-map-progress">
        <div className="flex items-center justify-between text-xs sm:text-sm mb-1">
          <span className="font-medium">Voortgang partnerschappen</span>
          <span className="tabular-nums" data-cy="partnership-progress-label">
            {completedCount}/{total} · {percentage}%
          </span>
        </div>

        <div className="h-2 rounded-full bg-gray-200 overflow-hidden">
          <div className="h-full bg-teal-500 transition-all" style={{ width: `${percentage}%` }} />
        </div>
      </div>

      <div className="w-full max-w-5xl mx-auto">
        <svg
          viewBox="0 0 1000 650"
          className="w-full h-auto border border-gray-100 rounded-xl bg-white max-w-full"
          role="img"
          aria-label="Overzicht van partnerschappen"
          data-cy="partnership-map-svg"
        >
          {/* Background image */}
          <image
            href="/images/partnership-layout.png"
            x={0}
            y={0}
            width={1000}
            height={650}
            preserveAspectRatio="xMidYMid meet"
          />

          {/* Outer oval */}
          <g transform={`rotate(${OUTER.rotation} ${OUTER.cx} ${OUTER.cy})`}>
            <ellipse
              cx={OUTER.cx}
              cy={OUTER.cy}
              rx={OUTER.rx}
              ry={OUTER.ry}
              fill="#dffbff"
              stroke="#14b8a6"
              strokeWidth={8}
            />
          </g>

          {/* Bubble shapes */}
          {BUBBLES.map((bubble, index) => {
            const cat = items[index];
            if (!cat) return null;

            const isChecked = decidedCategoryIds.has(cat.id);
            const circleStroke = "#14b8a6";
            const circleFill = isChecked ? "#14b8a6" : "none";
            const dashArray = isChecked ? "0" : "14 10";

            const bubbleId = cat.id;

            return (
              <g
                key={`shape-${bubbleId}`}
                onClick={() => onToggleCategory(cat.id)}
                style={{ cursor: "pointer" }}
                data-cy={`partnership-bubble-${bubbleId}`}
                data-checked={isChecked ? "true" : "false"}
              >
                <g transform={`rotate(${bubble.rotation} ${bubble.cx} ${bubble.cy})`}>
                  <ellipse
                    cx={bubble.cx}
                    cy={bubble.cy}
                    rx={bubble.rx}
                    ry={bubble.ry}
                    fill={circleFill}
                    stroke={circleStroke}
                    strokeWidth={4}
                    strokeDasharray={dashArray}
                  />

                  {/* Check circle */}
                  <circle
                    cx={bubble.cx + bubble.rx * 0.72}
                    cy={bubble.cy - bubble.ry * 0.72}
                    r={16}
                    fill="#ffffff"
                    stroke={isChecked ? "#14b8a6" : "#d1d5db"}
                    strokeWidth={3}
                  />
                  {isChecked && (
                    <text
                      x={bubble.cx + bubble.rx * 0.72}
                      y={bubble.cy - bubble.ry * 0.72 + 5}
                      fontSize={16}
                      textAnchor="middle"
                      fill="#14b8a6"
                      fontWeight="bold"
                    >
                      ✓
                    </text>
                  )}
                </g>
              </g>
            );
          })}

          {/* Labels (always on top) */}
          {BUBBLES.map((bubble, index) => {
            const cat = items[index];
            if (!cat) return null;

            const isChecked = decidedCategoryIds.has(cat.id);
            const label = (cat.label || "").trim();
            const textColor = isChecked ? "#ffffff" : "#111827";
            const fontSize = getLabelFontSize(label);

            return (
              <foreignObject
                key={`label-${cat.id}`}
                x={bubble.cx - bubble.rx + 10}
                y={bubble.cy - bubble.ry + 10}
                width={bubble.rx * 2 - 20}
                height={bubble.ry * 2 - 20}
                pointerEvents="none"
              >
                <div
                  style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    textAlign: "center",
                    width: "100%",
                    height: "100%",
                    fontSize: `${fontSize}px`,
                    lineHeight: 1.25,
                    color: textColor,
                    fontWeight: 700,
                    padding: "4px",
                    userSelect: "none",
                    wordBreak: "break-word",
                    hyphens: "auto",
                  }}
                >
                  {label}
                </div>
              </foreignObject>
            );
          })}
        </svg>
      </div>
    </section>
  );
};

export default PartnershipMap;
