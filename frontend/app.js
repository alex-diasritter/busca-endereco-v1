const API_URL = "http://localhost:8080";

// Elementos do DOM
let loginSection, appSection, loginForm, loginMessage, showRegisterLink, logoutBtn, searchBtn, cepInput, searchResult, addressesBody;

// Inicializa a aplicação quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', initApp);

function initApp() {
    console.log('Inicializando aplicação...');
    
    // Inicializar elementos do DOM
    loginSection = document.getElementById('login-section');
    appSection = document.getElementById('app');
    loginForm = document.getElementById('login-form');
    loginMessage = document.getElementById('login-message');
    showRegisterLink = document.getElementById('show-register');
    logoutBtn = document.getElementById('logout-btn');
    searchBtn = document.getElementById('search-btn');
    cepInput = document.getElementById('cep-input');
    searchResult = document.getElementById('search-result');
    addressesBody = document.getElementById('addresses-body');
    
    console.log('Elementos do DOM carregados:', {
        loginSection: !!loginSection,
        appSection: !!appSection,
        loginForm: !!loginForm,
        loginMessage: !!loginMessage,
        showRegisterLink: !!showRegisterLink,
        logoutBtn: !!logoutBtn,
        searchBtn: !!searchBtn,
        cepInput: !!cepInput,
        searchResult: !!searchResult,
        addressesBody: !!addressesBody
    });
    
    // Configurar eventos
    setupEventListeners();
    
    // Verificar autenticação
    checkAuth();
}

function setupEventListeners() {
    // Evento de login
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
    
    // Evento de logout
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
    
    // Evento de busca
    if (searchBtn) {
        searchBtn.addEventListener('click', searchAddress);
    }
    
    // Evento de tecla Enter no campo CEP
    if (cepInput) {
        cepInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchAddress();
            }
        });
    }
    
    // Link para registro
    if (showRegisterLink) {
        showRegisterLink.addEventListener('click', (e) => {
            e.preventDefault();
            window.location.href = 'register.html';
        });
    }
}

function checkAuth() {
    const token = localStorage.getItem('token');
    console.log('Verificando autenticação:', token ? 'Token encontrado' : 'Nenhum token encontrado');
    
    if (token) {
        showApp();
        loadAddresses();
    } else {
        showLogin();
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    
    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            throw new Error('Credenciais inválidas');
        }

        const { token } = await response.json();
        localStorage.setItem('token', token);
        showApp();
        loadAddresses();
    } catch (err) {
        console.error('Erro no login:', err);
        if (loginMessage) {
            loginMessage.textContent = err.message || 'Erro ao fazer login';
            loginMessage.className = 'error';
            loginMessage.style.display = 'block';
        }
    }
}

function handleLogout() {
    localStorage.removeItem('token');
    showLogin();
}

function showApp() {
    console.log('Mostrando aplicativo...');
    if (loginSection) loginSection.style.display = 'none';
    if (appSection) {
        appSection.style.display = 'block';
        appSection.classList.remove('hidden');
        console.log('Seção do aplicativo exibida');
    } else {
        console.error('Elemento appSection não encontrado');
    }
}

function showLogin() {
    console.log('Mostrando tela de login...');
    if (appSection) appSection.style.display = 'none';
    if (loginSection) {
        loginSection.style.display = 'block';
        loginSection.classList.remove('hidden');
        
        // Limpar campos de login
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        if (usernameInput) usernameInput.value = '';
        if (passwordInput) passwordInput.value = '';
        
        console.log('Tela de login exibida');
    } else {
        console.error('Elemento loginSection não encontrado');
    }
}

async function searchAddress() {
    if (!cepInput) {
        console.error('Campo CEP não encontrado');
        return;
    }
    
    const cep = cepInput.value.replace(/\D/g, '');
    const token = localStorage.getItem('token');
    
    if (cep.length !== 8) {
        showMessage('CEP deve ter 8 dígitos', 'error');
        return;
    }

    try {
        console.log('Buscando CEP:', cep);
        
        const response = await fetch(`${API_URL}/buscacep/${cep}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            console.error('Erro na busca:', errorData);
            throw new Error(errorData.message || 'Erro ao buscar CEP');
        }

        const data = await response.json();
        
        if (data.erro) {
            showMessage('CEP não encontrado', 'error');
            return;
        }

        showMessage('Endereço encontrado com sucesso!', 'success');
        cepInput.value = '';
        loadAddresses(); // Atualiza a lista de endereços
    } catch (err) {
        console.error('Erro ao buscar endereço:', err);
        if (err.message.includes('401') || err.message.includes('403')) {
            localStorage.removeItem('token');
            showLogin();
            showMessage('Sessão expirada. Faça login novamente.', 'error');
        } else {
            showMessage('Erro ao buscar endereço: ' + err.message, 'error');
        }
    }
}

async function loadAddresses() {
    if (!addressesBody) {
        console.error('Elemento addressesBody não encontrado');
        return;
    }
    
    const token = localStorage.getItem('token');
    if (!token) {
        console.error('Nenhum token encontrado para carregar endereços');
        showLogin();
        return;
    }

    try {
        console.log('Carregando endereços...');
        const response = await fetch(`${API_URL}/buscacep`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            console.error('Erro ao carregar endereços:', errorData);
            throw new Error(errorData.message || 'Erro ao carregar endereços');
        }

        const addresses = await response.json();
        console.log('Endereços carregados:', addresses);
        renderAddresses(addresses);
    } catch (err) {
        console.error('Erro ao carregar endereços:', err);
        if (err.message.includes('401') || err.message.includes('403')) {
            localStorage.removeItem('token');
            showLogin();
            showMessage('Sessão expirada. Faça login novamente.', 'error');
        } else {
            showMessage('Erro ao carrenar endereços: ' + err.message, 'error');
        }
    }
}

function renderAddresses(addresses) {
    if (!addressesBody) return;
    
    if (!addresses || addresses.length === 0) {
        addressesBody.innerHTML = '<tr><td colspan="4">Nenhum endereço encontrado</td></tr>';
        return;
    }

    addressesBody.innerHTML = addresses.map(address => `
        <tr>
            <td>${address.cep || '-'}</td>
            <td>${address.logradouro || '-'}</td>
            <td>${address.bairro || '-'}</td>
            <td>${address.localidade || '-'}/${address.uf || '-'}</td>
        </tr>
    `).join('');
}

function showMessage(message, type = 'error') {
    console.log(`${type.toUpperCase()}: ${message}`);
    
    if (!searchResult) {
        console.error('Elemento searchResult não encontrado para exibir mensagem');
        return;
    }
    
    const messageDiv = document.createElement('div');
    messageDiv.className = type;
    messageDiv.textContent = message;
    
    searchResult.innerHTML = '';
    searchResult.appendChild(messageDiv);
    
    // Remove a mensagem após 5 segundos
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

// Função global para voltar ao login (usada no register.html)
window.backToLogin = function() {
    window.location.href = 'index.html';
    return false;
};
