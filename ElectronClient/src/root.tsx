import { BrowserRouter, Route, Routes } from "react-router-dom";
import MainWindow from "./routes/main_window";

export default function Root() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/main_window" element={<MainWindow />} />
            </Routes>
        </BrowserRouter>
    )
}