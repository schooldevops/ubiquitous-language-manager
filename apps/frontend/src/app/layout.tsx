import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "AULMS",
  description: "데이터 사전 기반 유비쿼터스 랭기지 관리 시스템",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>{children}</body>
    </html>
  );
}

