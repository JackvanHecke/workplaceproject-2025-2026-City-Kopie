import React from "react";

export default function Map() {
  return (
    <section className="mt-8">
      <div className="w-full overflow-hidden rounded-2xl ring-1 ring-black/5">
        <iframe
          title="IPitup Map"
          src="https://dashboard.ipitup.be/mapbox"
          className="w-full h-[550px] md:h-[650px] lg:h-[700px]"
          loading="lazy"
          referrerPolicy="no-referrer-when-downgrade"
        />
      </div>
    </section>
  );
}
