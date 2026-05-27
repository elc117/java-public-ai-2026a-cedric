const API = '/api';

let strategies = [];
let ultimaConfig = null;

async function carregarFuncionarios() {
  const r = await fetch(`${API}/employees`);
  const lista = await r.json();
  const tbody = document.querySelector('#tabelaFuncionarios tbody');
  tbody.innerHTML = '';
  for (const e of lista) {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${e.nome}</td>
      <td>${e.cargo}</td>
      <td>${e.senioridade}</td>
      <td>${e.especialidade}</td>
      <td>${e.horasDisponiveis}h</td>
      <td><button class="btn-remover" data-id="${e.id}">remover</button></td>
    `;
    tbody.appendChild(tr);
  }
  tbody.querySelectorAll('.btn-remover').forEach(btn => {
    btn.addEventListener('click', () => removerFuncionario(btn.dataset.id));
  });
}

async function removerFuncionario(id) {
  await fetch(`${API}/employees/${id}`, { method: 'DELETE' });
  carregarFuncionarios();
}

async function carregarEnums() {
  const [cargos, senioridades] = await Promise.all([
    fetch(`${API}/cargos`).then(r => r.json()),
    fetch(`${API}/senioridades`).then(r => r.json()),
  ]);
  const selCargo = document.querySelector('select[name="cargo"]');
  const selSen = document.querySelector('select[name="senioridade"]');
  selCargo.innerHTML = cargos.map(c => `<option value="${c}">${c}</option>`).join('');
  selSen.innerHTML = senioridades.map(s => `<option value="${s}">${s}</option>`).join('');
}

async function carregarEstrategias() {
  const r = await fetch(`${API}/strategies`);
  strategies = await r.json();
  const sel = document.getElementById('selectStrategy');
  sel.innerHTML = strategies.map(s => `<option value="${s.id}">${s.nome}</option>`).join('');
  atualizarDescricao();
  sel.addEventListener('change', atualizarDescricao);
}

function atualizarDescricao() {
  const id = document.getElementById('selectStrategy').value;
  const s = strategies.find(x => x.id === id);
  document.getElementById('descStrategy').textContent = s ? s.descricao : '';
}

document.getElementById('formFuncionario').addEventListener('submit', async (ev) => {
  ev.preventDefault();
  const form = ev.target;
  const dados = Object.fromEntries(new FormData(form).entries());
  dados.horasDisponiveis = Number(dados.horasDisponiveis);
  await fetch(`${API}/employees`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dados),
  });
  form.reset();
  carregarFuncionarios();
});

document.getElementById('formRecomendar').addEventListener('submit', async (ev) => {
  ev.preventDefault();
  await recomendar();
});

document.getElementById('btnGerarNovamente').addEventListener('click', () => recomendar());

async function recomendar() {
  const strategy = document.getElementById('selectStrategy').value;
  const modo = document.getElementById('selectModo').value;
  const valor = Number(document.getElementById('inputValor').value);
  const body = { strategy };
  if (modo === 'tamanho') body.tamanhoEquipe = valor;
  else body.numeroEquipes = valor;

  ultimaConfig = body;

  const r = await fetch(`${API}/squads/recomendar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  if (!r.ok) {
    alert('Erro: ' + (await r.text()));
    return;
  }
  const data = await r.json();
  renderizarSquads(data.squads);
  document.getElementById('btnGerarNovamente').disabled = false;
}

function renderizarSquads(squads) {
  const card = document.getElementById('cardResultado');
  const div = document.getElementById('resultado');
  card.hidden = false;
  div.innerHTML = squads.map(s => `
    <div class="squad">
      <h3>${s.nome}</h3>
      <ul>
        ${s.membros.map(m => `
          <li>${m.nome}
            <span class="tag">${m.cargo}</span>
            <span class="tag">${m.senioridade}</span>
            <span class="tag">${m.especialidade}</span>
            <span class="tag">${m.horasDisponiveis}h</span>
          </li>
        `).join('')}
      </ul>
    </div>
  `).join('');
}

carregarEnums().then(carregarFuncionarios);
carregarEstrategias();
