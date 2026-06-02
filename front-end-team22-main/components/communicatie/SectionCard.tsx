import React from "react";

type Props = {
  title: string;
  children: React.ReactNode;
};

const SectionCard: React.FC<Props> = ({ title, children }) => {
  return (
    <section className="border border-gray-200 rounded-lg bg-gray-50 px-4 py-3 space-y-3">
      <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wide">
        {title}
      </h3>
      <div className="space-y-3">{children}</div>
    </section>
  );
};

export default SectionCard;
