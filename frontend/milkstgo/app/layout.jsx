import "@/styles/globals.css";
import Navbar from "@/components/Navbar";

export const metadata = {
    title: "MilkStgo",
};

const RootLayout = ({ children }) => {
    return (
        <html lang="es">
            <link rel="shortcut icon" href="/images/favicon.ico" />
            <link
                rel="apple-touch-icon"
                sizes="180x180"
                href="/images/apple-touch-icon.png"
            />
            <link
                rel="icon"
                type="image/png"
                sizes="32x32"
                href="/images/favicon-32x32.png"
            />
            <link
                rel="icon"
                type="image/png"
                sizes="16x16"
                href="/images/favicon-16x16.png"
            />
            <body>
                <Navbar />
                <main className="app">{children}</main>
            </body>
        </html>
    );
};

export default RootLayout;
