import { useEffect } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "@/pages/Home";
import ChatList from "@/pages/ChatList";
import ChatDetail from "@/pages/ChatDetail";
import ChatSettings from "@/pages/ChatSettings";
import { useTaskStore } from "@/store/useTaskStore";

export default function App() {
  const theme = useTaskStore((s) => s.theme);
  const accentColor = useTaskStore((s) => s.accentColor);

  useEffect(() => {
    if (theme === "dark") {
      document.documentElement.classList.add("dark");
    } else {
      document.documentElement.classList.remove("dark");
    }
  }, [theme]);

  useEffect(() => {
    document.documentElement.setAttribute("data-accent", accentColor);
  }, [accentColor]);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/chat" element={<ChatList />} />
        <Route path="/chat/:id" element={<ChatDetail />} />
        <Route path="/chat/settings" element={<ChatSettings />} />
      </Routes>
    </Router>
  );
}
