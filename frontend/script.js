const API_URL = "http://localhost:8080";

// Função para fazer login
async function fazerLogin(event) {
  event.preventDefault();

  const username = document.getElementById("username").value.trim();
  const password = document.getElementById("password").value.trim();
  const loginMsg = document.getElementById("loginMsg");

  if (!username || !password) {
    loginMsg.textContent = "Por favor, preencha todos os campos";
    loginMsg.style.color = "red";
    return;
  }

  try {
    const response = await fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || "Credenciais inválidas");
    }

    const { token } = await response.json();
    localStorage.setItem("token", token);
    window.location.href = "dashboard.html";
  } catch (err) {
    loginMsg.textContent = err.message;
    loginMsg.style.color = "red";
  }
}

// Função para chamadas autenticadas
async function authFetch(url, options = {}) {
  const token = localStorage.getItem("token");
  if (!token) {
    logout();
    throw new Error("Sessão expirada");
  }

  const headers = {
    ...(options.headers || {}),
    "Authorization": `Bearer ${token}`
  };

  if (options.body && typeof options.body === 'object') {
    headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(options.body);
  }

  const response = await fetch(url, { ...options, headers });

  if (!response.ok) {
    if (response.status === 401 || response.status === 403) {
      logout();
      alert("Sessão expirada, faça login novamente.");
    }

    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.message || `Erro ${response.status}`);
  }

  return response.json();
}

// Logout
function logout() {
  localStorage.removeItem("token");
  window.location.href = "index.html";
}

// Buscar endereço por CEP
async function buscarEndereco() {
  const cepInput = document.getElementById("cepInput");
  const resultadoDiv = document.getElementById("resultadoBusca");
  const cep = cepInput.value.replace(/\D/g, '');

  if (cep.length !== 8) {
    resultadoDiv.textContent = "CEP inválido. Digite 8 dígitos.";
    resultadoDiv.style.color = "red";
    return;
  }

  try {
    resultadoDiv.innerHTML = '<div class="loader"></div>';
    const data = await authFetch(`${API_URL}/buscacep/${cep}`);

    resultadoDiv.innerHTML = `
      <p><strong>CEP:</strong> ${data.cep || "-"}</p>
      <p><strong>Logradouro:</strong> ${data.logradouro || "-"}</p>
      <p><strong>Bairro:</strong> ${data.bairro || "-"}</p>
      <p><strong>Cidade:</strong> ${data.localidade || "-"}</p>
      <p><strong>UF:</strong> ${data.uf || "-"}</p>
    `;
    resultadoDiv.style.color = "black";
    cepInput.value = "";
  } catch (err) {
    resultadoDiv.textContent = "Erro ao buscar endereço: " + err.message;
    resultadoDiv.style.color = "red";
  }
}

// Carregar histórico de buscas
async function carregarHistorico() {
  const tableBody = document.querySelector("#historico-table tbody");
  try {
    // Mostrar loader
    tableBody.innerHTML = '<tr><td colspan="2"><div class="loader"></div></td></tr>';

    const dados = await authFetch(`${API_URL}/buscas`);
    tableBody.innerHTML = "";

    dados.forEach(item => {
      const tr = document.createElement("tr");
      const dataFormatada = new Date(item.data).toLocaleString();
      tr.innerHTML = `<td>${dataFormatada}</td><td>${item.endereco || "-"}</td>`;
      tableBody.appendChild(tr);
    });
  } catch (err) {
    tableBody.innerHTML = '<tr><td colspan="2">Erro ao carregar histórico</td></tr>';
    console.error("Erro ao carregar histórico:", err);
  }
}

// Carregar endereços salvos
async function carregarEnderecos() {
  const tableBody = document.querySelector("#enderecos-table tbody");
  try {
    // Mostrar loader
    tableBody.innerHTML = '<tr><td colspan="5"><div class="loader"></div></td></tr>';

    const dados = await authFetch(`${API_URL}/buscacep`);
    tableBody.innerHTML = "";

    dados.forEach(endereco => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${endereco.cep || "-"}</td>
        <td>${endereco.logradouro || "-"}</td>
        <td>${endereco.bairro || "-"}</td>
        <td>${endereco.localidade || "-"}</td>
        <td>${endereco.uf || "-"}</td>
      `;
      tableBody.appendChild(tr);
    });
  } catch (err) {
    tableBody.innerHTML = '<tr><td colspan="5">Erro ao carregar endereços</td></tr>';
    console.error("Erro ao carregar endereços:", err);
  }
}

// Carregar usuários (somente admin)
async function carregarUsuarios() {
  const tableBody = document.querySelector("#usuarios-table tbody");
  try {
    // Mostrar loader
    tableBody.innerHTML = '<tr><td colspan="2"><div class="loader"></div></td></tr>';

    const dados = await authFetch(`${API_URL}/auth/users`);
    tableBody.innerHTML = "";

    dados.forEach(user => {
      const tr = document.createElement("tr");
      tr.innerHTML = `<td>${user.username}</td><td>${user.role}</td>`;
      tableBody.appendChild(tr);
    });
  } catch (err) {
    tableBody.innerHTML = '<tr><td colspan="2">Erro ao carregar usuários</td></tr>';
    console.error("Erro ao carregar usuários:", err);
  }
}

// Cadastrar novo usuário
async function cadastrarUsuario(event) {
  event.preventDefault();

  const form = event.target;
  const username = form.username.value.trim();
  const password = form.password.value.trim();
  const role = form.role.value;
  const msgDiv = document.getElementById("msg");

  try {
    msgDiv.textContent = "Cadastrando...";
    msgDiv.style.color = "blue";

    await authFetch(`${API_URL}/auth/register`, {
      method: "POST",
      body: { username, password, role },
    });

    msgDiv.textContent = "Usuário cadastrado com sucesso!";
    msgDiv.style.color = "green";
    form.reset();
    carregarUsuarios();
  } catch (err) {
    msgDiv.textContent = "Erro ao cadastrar usuário: " + err.message;
    msgDiv.style.color = "red";
  }
}

// Inicialização da dashboard
async function inicializarDashboard() {
  // Configurar logout
  document.getElementById("logoutBtn").onclick = logout;

  // Verificar autenticação
  if (!localStorage.getItem("token")) {
    window.location.href = "index.html";
    return;
  }

  // Configurar eventos
  document.getElementById("buscarBtn").onclick = buscarEndereco;
  document.getElementById("carregarHistoricoBtn").onclick = carregarHistorico;
  document.getElementById("carregarEnderecosBtn").onclick = carregarEnderecos;
  document.getElementById("carregarUsuariosBtn").onclick = carregarUsuarios;
  document.getElementById("cadastro-form").onsubmit = cadastrarUsuario;
}

// Inicializar após carregamento do DOM
document.addEventListener("DOMContentLoaded", () => {
  // Página de login
  const loginForm = document.getElementById("login-form");
  if (loginForm) {
    loginForm.addEventListener("submit", fazerLogin);
  }

  // Dashboard
  if (document.getElementById("logoutBtn")) {
    inicializarDashboard();
  }
});