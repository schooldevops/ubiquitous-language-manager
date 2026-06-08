"use client";

import { useEffect, useRef, useState } from "react";

let initialized = false;

/** Mermaid 코드(chart)를 클라이언트에서 SVG 다이어그램으로 렌더한다. */
export default function MermaidDiagram({ chart, id }: { chart: string; id: string }) {
  const ref = useRef<HTMLDivElement>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    // mermaid는 무거운 ESM 모듈이라 클라이언트에서 동적 import 한다.
    // (top-level import 시 webpack 청크 오류 "Cannot read properties of undefined" 발생)
    void (async () => {
      try {
        const mermaid = (await import("mermaid")).default;
        if (!initialized) {
          mermaid.initialize({ startOnLoad: false, theme: "neutral", securityLevel: "loose" });
          initialized = true;
        }
        const { svg } = await mermaid.render(`mermaid-${id}`, chart);
        if (!cancelled && ref.current) ref.current.innerHTML = svg;
      } catch (err) {
        if (!cancelled) setError(err instanceof Error ? err.message : "다이어그램 렌더 실패");
      }
    })();
    return () => {
      cancelled = true;
    };
  }, [chart, id]);

  if (error) {
    return <pre className="overflow-auto rounded-md bg-[#fdecec] px-3 py-3 text-xs text-[var(--danger)]">{error}</pre>;
  }
  return <div ref={ref} className="overflow-x-auto" />;
}
