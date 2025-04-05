import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainWindow from "./routes/main_window";

export default function Root() {
    return (
        <>
        <head>
            <meta httpEquiv="Content-Security-Policy" content="default-src 'self';
            img-src data: https: http:;
            script-src 'self' 'unsafe-inline';
            style-src 'self' 'unsafe-inline';
            connect-src 'self' http://localhost:1080;"/>
        </head>
        <BrowserRouter>
            <Routes>
                <Route path="/main_window" element={<MainWindow />} />
            </Routes>
        </BrowserRouter>
        </>
    )
}