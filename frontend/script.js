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

  // Verificar se o token existe
  if (!token) {
    logout();
    throw new Error("Token não encontrado, faça login.");
  }

  const headers = {
    ...(options.headers || {}),
    "Authorization": `Bearer ${token}`
  };

  // Não forçar Content-Type para requisições sem corpo
  if (options.body && typeof options.body === 'object' && !headers["Content-Type"]) {
    headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(options.body);
  }

  try {
    const response = await fetch(url, { ...options, headers });

    if (response.status === 401) {
      // Token expirado ou inválido
      logout();
      throw new Error("Sessão expirada, faça login novamente.");
    }

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || `Erro ${response.status}: ${response.statusText}`);
    }

    return response.json();
  } catch (err) {
    if (err.message.includes("Sessão expirada")) {
      // Já tratado pelo logout, não precisa fazer nada extra
      throw err;
    }

    if (err.message.includes("Failed to fetch")) {
      throw new Error("Erro de conexão com o servidor");
    }

    throw err;
  }
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
  
  if (!cepInput || !resultadoDiv) {
    console.error("Elementos necessários não encontrados.");
    return;
  }
  
  const cep = cepInput.value.replace(/\D/g, '');

  // Validação básica do CEP
  if (cep.length !== 8) {
    resultadoDiv.innerHTML = `
      <div class="alert alert-warning">
        <strong>CEP inválido!</strong> O CEP deve conter exatamente 8 dígitos.
      </div>
    `;
    return;
  }

  try {
    // Mostrar loader
    resultadoDiv.innerHTML = `
      <div class="d-flex justify-content-center">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Buscando...</span>
        </div>
        <span class="ms-2">Buscando endereço...</span>
      </div>
    `;
    
    // Fazer a requisição
    const data = await authFetch(`${API_URL}/buscacep/${cep}`);
    
    // Verificar se o CEP foi encontrado
    if (data.erro) {
      throw new Error("CEP não encontrado");
    }

    // Exibir os dados formatados
    resultadoDiv.innerHTML = `
      <div class="card">
        <div class="card-header bg-primary text-white">
          <h5 class="mb-0">Endereço encontrado</h5>
        </div>
        <div class="card-body">
          <p class="mb-2"><strong class="text-primary">CEP:</strong> ${data.cep || "-"}</p>
          <p class="mb-2"><strong class="text-primary">Logradouro:</strong> ${data.logradouro || "-"}</p>
          <p class="mb-2"><strong class="text-primary">Bairro:</strong> ${data.bairro || "-"}</p>
          <p class="mb-2"><strong class="text-primary">Cidade/UF:</strong> ${data.localidade || "-"}/${data.uf || "-"}</p>
          ${data.complemento ? `<p class="mb-0"><strong class="text-primary">Complemento:</strong> ${data.complemento}</p>` : ''}
        </div>
      </div>
    `;
    
    // Limpar o campo de entrada
    cepInput.value = "";
    
    // Recarregar o histórico de buscas
    await carregarHistorico();
    
  } catch (err) {
    console.error("Erro ao buscar endereço:", err);
    
    let errorMessage = "Erro ao buscar endereço";
    if (err.message.includes("404")) {
      errorMessage = "CEP não encontrado. Verifique o número e tente novamente.";
    } else if (err.message.includes("network")) {
      errorMessage = "Não foi possível conectar ao servidor. Verifique sua conexão com a internet.";
    } else if (err.message.includes("401") || err.message.includes("403")) {
      errorMessage = "Sessão expirada. Por favor, faça login novamente.";
      logout();
    }
    
    resultadoDiv.innerHTML = `
      <div class="alert alert-danger">
        <strong>Erro!</strong> ${errorMessage}
      </div>
    `;
  } finally {
    // Garantir que o campo de CEP mantenha o foco para uma nova busca
    cepInput.focus();
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

    if (dados.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="2">Nenhuma busca encontrada</td></tr>';
      return;
    }

    dados.forEach(item => {
      const tr = document.createElement("tr");
      // Usando dataHoraBusca em vez de data
      const dataHora = item.dataHoraBusca ? new Date(item.dataHoraBusca) : null;
      const dataFormatada = dataHora ? dataHora.toLocaleString('pt-BR') : 'Data não disponível';
      tr.innerHTML = `<td>${dataFormatada}</td><td>${item.usersame || "-"}</td>`;
      tableBody.appendChild(tr);
    });
  } catch (err) {
    console.error("Erro detalhado ao carregar histórico:", err);
    tableBody.innerHTML = '<tr><td colspan="2">Erro ao carregar histórico. Tente novamente.</td></tr>';
  }
}

// Carregar endereços salvos
async function carregarEnderecos() {
  const tableBody = document.querySelector("#enderecos-table tbody");
  if (!tableBody) return; // Se não encontrar a tabela, sai da função
  
  try {
    // Mostrar loader
    tableBody.innerHTML = '<tr><td colspan="5"><div class="loader">Carregando endereços...</div></td></tr>';

    const dados = await authFetch(`${API_URL}/buscacep`);
    
    if (!dados || dados.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="5">Nenhum endereço encontrado</td></tr>';
      return;
    }

    tableBody.innerHTML = ""; // Limpa o loader

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
    console.error("Erro detalhado ao carregar endereços:", err);
    tableBody.innerHTML = `
      <tr>
        <td colspan="5" class="error-message">
          Erro ao carregar endereços. ${err.message || 'Tente novamente mais tarde.'}
        </td>
      </tr>`;
  }
}

// Carregar usuários (somente admin)
async function carregarUsuarios() {
  const tableBody = document.querySelector("#usuarios-table tbody");
  if (!tableBody) return; // Se não encontrar a tabela, sai da função

  try {
    // Mostrar loader
    tableBody.innerHTML = '<tr><td colspan="3"><div class="loader">Carregando usuários...</div></td></tr>';

    const dados = await authFetch(`${API_URL}/users`);
    
    if (!dados || dados.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="3">Nenhum usuário cadastrado</td></tr>';
      return;
    }

    tableBody.innerHTML = ""; // Limpa o loader

    dados.forEach(usuario => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${usuario.id || "-"}</td>
        <td>${usuario.username || "-"}</td>
        <td>${usuario.role || "-"}</td>
      `;
      tableBody.appendChild(tr);
    });
  } catch (err) {
    console.error("Erro detalhado ao carregar usuários:", err);
    const errorMessage = err.status === 403 
      ? 'Acesso negado. Apenas administradores podem visualizar esta lista.'
      : `Erro ao carregar usuários. ${err.message || 'Tente novamente mais tarde.'}`;
      
    tableBody.innerHTML = `
      <tr>
        <td colspan="3" class="error-message">
          ${errorMessage}
        </td>
      </tr>`;
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
  try {
    // Verificar autenticação
    if (!localStorage.getItem("token")) {
      window.location.href = "index.html";
      return;
    }

    // Configurar logout
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
      logoutBtn.onclick = logout;
    }

    // Configurar eventos apenas se os elementos existirem
    const buscarBtn = document.getElementById("buscarBtn");
    if (buscarBtn) buscarBtn.onclick = buscarEndereco;
    
    const carregarHistoricoBtn = document.getElementById("carregarHistoricoBtn");
    if (carregarHistoricoBtn) carregarHistoricoBtn.onclick = carregarHistorico;
    
    const carregarEnderecosBtn = document.getElementById("carregarEnderecosBtn");
    if (carregarEnderecosBtn) carregarEnderecosBtn.onclick = carregarEnderecos;
    
    const carregarUsuariosBtn = document.getElementById("carregarUsuariosBtn");
    if (carregarUsuariosBtn) carregarUsuariosBtn.onclick = carregarUsuarios;
    
    const cadastroForm = document.getElementById("cadastro-form");
    if (cadastroForm) cadastroForm.onsubmit = cadastrarUsuario;
    
    // Carregar dados iniciais se estiver na página correta
    const currentPage = window.location.pathname.split('/').pop();
    if (currentPage === 'dashboard.html') {
      // Carregar histórico por padrão
      await carregarHistorico();
    }
  } catch (error) {
    console.error("Erro ao inicializar o dashboard:", error);
    // Exibir mensagem de erro amigável para o usuário
    const errorDiv = document.createElement('div');
    errorDiv.className = 'alert alert-danger';
    errorDiv.textContent = 'Ocorreu um erro ao carregar o dashboard. Por favor, tente novamente.';
    document.body.prepend(errorDiv);
  }
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