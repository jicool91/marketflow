import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";

const data = [
    { date: "Пн", leads: 12 },
    { date: "Вт", leads: 18 },
    { date: "Ср", leads: 9 },
    { date: "Чт", leads: 24 },
    { date: "Пт", leads: 14 },
    { date: "Сб", leads: 7 },
    { date: "Вс", leads: 19 },
];

export default function Dashboard() {
    return (
        <div className="min-h-screen bg-gray-100 p-6">
            <h1 className="text-3xl font-bold mb-6">📊 Метрики по лидам</h1>
            <div className="bg-white p-4 rounded-xl shadow-xl">
                <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="date" />
                        <YAxis />
                        <Tooltip />
                        <Line type="monotone" dataKey="leads" stroke="#8884d8" strokeWidth={2} />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
}
