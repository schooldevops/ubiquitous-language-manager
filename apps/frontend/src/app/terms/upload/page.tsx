"use client";

import { useState } from "react";
import type { TermUploadResult } from "@/lib/term-api";
import { uploadTermsFile } from "@/lib/term-api";

export default function TermUploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [result, setResult] = useState<TermUploadResult | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function onUpload() {
    if (!file) return;
    setLoading(true);
    setError(null);
    try {
      setResult(await uploadTermsFile(file));
    } catch (e) {
      setError(e instanceof Error ? e.message : "업로드 실패");
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="mx-auto max-w-4xl p-8">
      <h1 className="text-2xl font-bold mb-4">용어 JSONL 업로드</h1>

      <div className="flex items-center gap-3 mb-6">
        <input
          type="file"
          accept=".jsonl,application/jsonl,text/plain"
          onChange={(e) => setFile(e.target.files?.[0] ?? null)}
          className="border rounded px-3 py-2"
        />
        <button
          onClick={onUpload}
          disabled={!file || loading}
          className="rounded bg-blue-600 px-4 py-2 text-white disabled:opacity-50"
        >
          {loading ? "업로드 중..." : "업로드"}
        </button>
      </div>

      {error && <p className="text-red-600 mb-4">{error}</p>}

      {result && (
        <section>
          <div className="flex gap-2 mb-4 text-sm">
            <Badge label={`전체 ${result.totalRows}`} cls="bg-gray-200 text-gray-800" />
            <Badge label={`삽입 ${result.inserted}`} cls="bg-green-200 text-green-900" />
            <Badge label={`수정 ${result.updated}`} cls="bg-blue-200 text-blue-900" />
            <Badge label={`실패 ${result.failed}`} cls="bg-red-200 text-red-900" />
          </div>
          <p className="text-xs text-gray-500 mb-2">batch: {result.uploadBatchId}</p>
          <table className="w-full text-sm border">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-2 text-left">#</th>
                <th className="p-2 text-left">termId</th>
                <th className="p-2 text-left">status</th>
                <th className="p-2 text-left">error</th>
                <th className="p-2 text-left">등록일시</th>
                <th className="p-2 text-left">실패일시</th>
              </tr>
            </thead>
            <tbody>
              {result.rows.map((r) => (
                <tr key={r.lineNo} className={r.status === "FAILED" ? "bg-red-50" : ""}>
                  <td className="p-2">{r.lineNo}</td>
                  <td className="p-2">{r.termId ?? "-"}</td>
                  <td className="p-2 font-medium">{r.status}</td>
                  <td className="p-2 text-red-600">{r.errorMessage ?? ""}</td>
                  <td className="p-2">{r.registeredAt ?? ""}</td>
                  <td className="p-2">{r.failedAt ?? ""}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>
      )}
    </main>
  );
}

function Badge({ label, cls }: { label: string; cls: string }) {
  return <span className={`rounded px-2 py-1 ${cls}`}>{label}</span>;
}
