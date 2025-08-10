const API_URL = "http://localhost:8080";

// DOM Elements
const loginSection = document.getElementById('login-section');
const appSection = document.getElementById('app');
const loginForm = document.getElementById('login-form');
const loginMessage = document.getElementById('login-message');
const registerSection = document.getElementById('register-section');
const registerForm = document.getElementById('register-form');
const registerMessage = document.getElementById('register-message');
const showRegisterLink = document.getElementById('show-register');
const logoutBtn = document.getElementById('logout-btn');
const searchBtn = document.getElementById('search-btn');
const cepInput = document.getElementById('cep-input');
const searchResult = document.getElementById('search-result');
const addressesBody = document.getElementById('addresses-body');

// Check authentication on load
document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
    console.log('Token on load:', token ? 'Token exists' : 'No token found');
    
    if (token) {
        showApp();
        loadAddresses();
    } else {
        showLogin();
    }
});

// Login
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
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
            showMessage('Usuário ou senha inválidos', 'error');
            console.error('Login error:', err);
        }
    });
}

// Logout
if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
        localStorage.removeItem('token');
        showLogin();
    });
}

// Search CEP
if (searchBtn) {
    searchBtn.addEventListener('click', searchAddress);
}

// Funções globais para navegação
window.showRegister = function() {
    if (loginSection) loginSection.style.display = 'none';
    if (registerSection) registerSection.style.display = 'block';
};

window.backToLogin = function() {
    if (registerSection) registerSection.style.display = 'none';
    if (loginSection) loginSection.style.display = 'block';
    clearRegisterForm();
    return false;
};

// Toggle between login and register forms
if (showRegisterLink) {
    showRegisterLink.onclick = showRegister;
}

// Handle register form submission
if (registerForm) {
    registerForm.addEventListener('submit', handleRegister);
}

// Allow pressing Enter in CEP input
if (cepInput) {
    cepInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            searchAddress();
        }
    });
}

async function searchAddress() {
    const cep = cepInput.value.replace(/\D/g, '');
    const token = localStorage.getItem('token');
    
    if (cep.length !== 8) {
        showMessage('CEP deve ter 8 dígitos', 'error');
        return;
    }

    try {
        console.log('Searching CEP:', cep);
        console.log('Using token:', token);
        
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
            console.error('Search error response:', errorData);
            throw new Error(errorData.message || 'Erro ao buscar CEP');
        }

        const data = await response.json();
        
        if (data.erro) {
            showMessage('CEP não encontrado', 'error');
            return;
        }

        showMessage('Endereço encontrado com sucesso!', 'success');
        cepInput.value = '';
        loadAddresses(); // Refresh the addresses list
    } catch (err) {
        if (err.message.includes('401')) {
            showLogin();
            showMessage('Sessão expirada. Faça login novamente.', 'error');
        } else {
            showMessage('Erro ao buscar endereço: ' + err.message, 'error');
            console.error('Search error:', err);
        }
    }
}

async function loadAddresses() {
    const token = localStorage.getItem('token');
    if (!token) {
        console.error('No token found for loading addresses');
        showLogin();
        return;
    }

    try {
        console.log('Loading addresses with token:', token);
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
            console.error('Load addresses error response:', errorData);
            throw new Error(errorData.message || 'Erro ao carregar endereços');
        }

        const addresses = await response.json();
        console.log('Addresses loaded:', addresses);
        renderAddresses(addresses);
    } catch (err) {
        console.error('Error loading addresses:', err);
        if (err.message.includes('401') || err.message.includes('403')) {
            localStorage.removeItem('token');
            showLogin();
            showMessage('Sessão expirada. Faça login novamente.', 'error');
        } else {
            showMessage('Erro ao carregar endereços: ' + err.message, 'error');
        }
    }
}

function renderAddresses(addresses) {
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
    const messageDiv = document.createElement('div');
    messageDiv.className = type;
    messageDiv.textContent = message;
    
    searchResult.innerHTML = '';
    searchResult.appendChild(messageDiv);
    
    // Remove message after 5 seconds
    setTimeout(() => {
        messageDiv.remove();
    }, 5000);
}

function showApp() {
    loginSection.style.display = 'none';
    appSection.style.display = 'block';
}

async function handleRegister(e) {
    e.preventDefault();
    
    const username = document.getElementById('reg-username').value.trim();
    const password = document.getElementById('reg-password').value;

    
    if (password.length < 4) {
        showRegisterMessage('A senha deve ter pelo menos 4 caracteres', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                username: username.trim(),
                password: password,
                role: 'USER' // Default role, must match UserRole enum in backend
            })
        });
        
        const data = await response.json();
        
        if (!response.ok) {
            const errorMessage = data.message || 'Erro ao cadastrar usuário';
            throw new Error(errorMessage);
        }
        
        showRegisterMessage('Usuário cadastrado com sucesso! Faça login para continuar.', 'success');
        
        // Clear form and switch to login after 2 seconds
        setTimeout(() => {
            clearRegisterForm();
            registerSection.style.display = 'none';
            loginSection.style.display = 'block';
            showRegisterMessage('', '');
        }, 2000);
        
    } catch (err) {
        console.error('Registration error:', err);
        showRegisterMessage(err.message || 'Erro ao cadastrar usuário', 'error');
    }
}

function showRegisterMessage(message, type = 'error') {
    if (!registerMessage) return;
    
    registerMessage.textContent = message;
    registerMessage.className = type;
    
    if (type === 'success') {
        registerMessage.style.color = 'green';
    } else {
        registerMessage.style.color = 'red';
    }
}

function clearRegisterForm() {
    if (registerForm) registerForm.reset();
    if (registerMessage) registerMessage.textContent = '';
}

function showLogin() {
    loginSection.style.display = 'block';
    registerSection.style.display = 'none';
    appSection.style.display = 'none';
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
    if (loginMessage) loginMessage.textContent = '';
}
