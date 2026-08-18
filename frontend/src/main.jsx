import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { request } from './api';
import './style.css';

const priorities = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
const statuses = ['NEW', 'IN_PROGRESS', 'WAITING_USER', 'RESOLVED', 'CLOSED'];

function App() {
  const [login, setLogin] = useState({ username: 'user', password: 'password' });
  const [me, setMe] = useState(null); const [tickets, setTickets] = useState([]); const [specialists, setSpecialists] = useState([]);
  const [form, setForm] = useState({ title: '', category: 'Рабочее место', description: '', priority: 'MEDIUM' });
  const [error, setError] = useState('');
  const isIt = me && ['IT_SPECIALIST', 'ADMIN'].includes(me.role);
  const load = async () => { setError(''); const user = await request('/me', login); setMe(user); setTickets(await request(user.role === 'USER' ? '/tickets' : '/it/tickets', login)); if (user.role !== 'USER') setSpecialists(await request('/it/specialists', login)); };
  useEffect(() => { load().catch(e => setError(e.message)); }, []);
  const create = async (e) => { e.preventDefault(); await request('/tickets', login, { method: 'POST', body: JSON.stringify(form) }); setForm({ ...form, title: '', description: '' }); await load(); };
  const update = async (id, patch) => { await request(`/it/tickets/${id}`, login, { method: 'PUT', body: JSON.stringify(patch) }); await load(); };
  const comment = async (id, text) => { if (text) { await request(`/it/tickets/${id}/comments`, login, { method: 'POST', body: JSON.stringify({ text }) }); await load(); } };
  return <main>
    <h1>Внутренний портал заявок</h1>
    <section className="card"><h2>Вход</h2><input value={login.username} onChange={e=>setLogin({...login,username:e.target.value})}/><input type="password" value={login.password} onChange={e=>setLogin({...login,password:e.target.value})}/><button onClick={load}>Войти</button>{me && <b>{me.fullName} · {me.role}</b>}{error && <p className="error">{error}</p>}</section>
    {!isIt && <section className="card"><h2>Создать заявку</h2><form onSubmit={create}><input required placeholder="Тема" value={form.title} onChange={e=>setForm({...form,title:e.target.value})}/><select value={form.category} onChange={e=>setForm({...form,category:e.target.value})}>{['Рабочее место','Сеть','ПО','Оборудование','Доступы'].map(x=><option key={x}>{x}</option>)}</select><select value={form.priority} onChange={e=>setForm({...form,priority:e.target.value})}>{priorities.map(x=><option key={x}>{x}</option>)}</select><textarea required placeholder="Описание проблемы" value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/><button>Отправить</button></form></section>}
    <section className="card"><h2>{isIt ? 'Панель IT: все заявки' : 'Мои заявки'}</h2><div className="grid">{tickets.map(t => <article key={t.id} className="ticket"><h3>#{t.id} {t.title}</h3><p>{t.category} · {t.priority} · <b>{t.status}</b></p><p>{t.description}</p><small>Заявитель: {t.requester.fullName}; Исполнитель: {t.assignee?.fullName || 'не назначен'}</small>{isIt && <div className="controls"><select value={t.status} onChange={e=>update(t.id,{status:e.target.value})}>{statuses.map(s=><option key={s}>{s}</option>)}</select><select value={t.assignee?.id || ''} onChange={e=>update(t.id,{assigneeId:Number(e.target.value)})}><option value="">Назначить</option>{specialists.map(s=><option value={s.id} key={s.id}>{s.fullName}</option>)}</select><input placeholder="Комментарий" onKeyDown={e=>{if(e.key==='Enter'){comment(t.id,e.currentTarget.value); e.currentTarget.value='';}}}/></div>}<details><summary>Комментарии и история</summary>{t.comments.map(c=><p key={c.id}>💬 {c.author.fullName}: {c.text}</p>)}{t.history.map(h=><p key={h.id}>🕘 {h.actor.fullName}: {h.fieldName} {h.oldValue || '∅'} → {h.newValue}</p>)}</details></article>)}</div></section>
  </main>;
}
createRoot(document.getElementById('root')).render(<App />);
