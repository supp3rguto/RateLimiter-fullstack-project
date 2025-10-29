import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const API_URL = 'http://localhost:8080/api';
const PLAN_MAX_TOKENS = 10;
const PLAN_REFILL_SECONDS = 60;

function App() {
    const [apiKey, setApiKey] = useState('augusto-dev-key');
    const [tokens, setTokens] = useState(PLAN_MAX_TOKENS);
    const [countdown, setCountdown] = useState(0);
    const [log, setLog] = useState([]);

    useEffect(() => {
        if (countdown <= 0) return;

        const timer = setInterval(() => {
            setCountdown(prev => prev - 1);
        }, 1000);

        return () => clearInterval(timer);
    }, [countdown]);

    useEffect(() => {
        if (countdown === 0) {
            setTokens(PLAN_MAX_TOKENS);
            addLog('...Recarga de tokens concluída!', 'log-success');
        }
    }, [countdown]);

    const addLog = (message, type = 'log-success') => {
        const timestamp = new Date().toLocaleTimeString();
        const newEntry = {
            id: crypto.randomUUID(),
            message: `[${timestamp}] ${message}`,
            type
        };
        setLog(prevLog => [newEntry, ...prevLog]);
    };

    const handleApiCall = async (endpoint, tokenCost = 0) => {
        if (countdown > 0 && endpoint !== 'public') {
            addLog(`[ERRO] Bloqueado. Tente novamente em ${countdown}s`, 'log-error');
            return;
        }

        if (tokenCost > 0 && tokens < tokenCost) {
            addLog(`[ERRO UI] Tokens insuficientes. (Necessário: ${tokenCost}, Restante: ${tokens})`, 'log-error');
            return;
        }

        try {
            const config = {
                headers: { 'X-API-KEY': apiKey }
            };

            const response = await axios.get(`${API_URL}/${endpoint}`, config);
            addLog(`[${response.status} OK] ${response.data}`, 'log-success');

            if (tokenCost > 0) {
                setTokens(prev => Math.max(0, prev - tokenCost));
            }

        } catch (error) {
            if (!error.response) {
                addLog(`Erro de rede ou CORS: ${error.message}`, 'log-error');
                return;
            }

            const { status, data } = error.response;

            if (status === 429) {
                addLog(`[${status} ERRO] ${data}`, 'log-error');
                setTokens(0);
                setCountdown(PLAN_REFILL_SECONDS);
            }
            else if (status === 401) {
                addLog(`[${status} ERRO] ${data}`, 'log-unauthorized');
                setTokens(0);
            }
            else {
                addLog(`[${status} ERRO] ${data}`, 'log-error');
            }
        }
    };

    return (
        <div className="App">
            <h1>Dashboard do Rate Limiter</h1>
            <p>Simulador de um cliente SaaS consumindo uma API protegida.</p>

            <div className="dashboard">

                <div className="api-key-input">
                    <label htmlFor="api-key">Sua Chave de API (X-API-KEY)</label>
                    <input
                        id="api-key"
                        type="text"
                        value={apiKey}
                        onChange={(e) => setApiKey(e.target.value)}
                    />
                </div>

                <div className="controls">
                    <button onClick={() => handleApiCall('public', 0)}>
                        Chamar API Pública (Livre)
                    </button>
                    <button
                        onClick={() => handleApiCall('limited', 1)}
                        disabled={countdown > 0}
                    >
                        Chamar API (Custa 1 Token)
                    </button>
                    <button
                        style={{ background: '#d50000' }}
                        onClick={() => handleApiCall('create-resource', 5)}
                        disabled={countdown > 0}
                    >
                        Criar Recurso (Custa 5 Tokens)
                    </button>
                </div>

                {countdown > 0 && (
                    <div className="countdown">
                        BLOQUEADO! Tente novamente em {countdown} segundos...
                    </div>
                )}

                <div className="token-status">
                    <label htmlFor="token-bar">Tokens Restantes (Plano Free): {tokens} / {PLAN_MAX_TOKENS}</label>
                    <progress id="token-bar" value={tokens} max={PLAN_MAX_TOKENS}></progress>
                </div>
            </div>

            <h3>Log de Requisições:</h3>
            <div className="log-container">
                {log.map((entry) => (
                    <div key={entry.id} className={`log-entry ${entry.type}`}>
                        {entry.message}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default App;

