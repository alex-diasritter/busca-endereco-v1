// A URL da API do backend, acessível pelo navegador
const API_URL = "http://localhost:8080";

/**
 * Realiza o login do usuário.
 */
async function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const msgDiv = document.getElementById("loginMsg");

    try {
        const resp = await fetch(`${API_URL}/auth/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (resp.ok) {
            const data = await resp.json();
            localStorage.setItem("token", data.token);
            msgDiv.innerHTML = "<span style='color:green;'>Login realizado! Redirecionando...</span>";
            // Redireciona para a página de busca após 1 segundo
            window.location.href = "buscar.html";
        } else {
            msgDiv.innerHTML = "<span style='color:red;'>Usuário ou senha inválidos!</span>";
        }
    } catch (error) {
        console.error("Erro ao fazer login:", error);
        msgDiv.innerHTML = "<span style='color:red;'>Falha na comunicação com o servidor.</span>";
    }
}

/**
 * Busca um endereço a partir de um CEP.
 */
async function buscarEndereco() {
    const cep = document.getElementById("cep").value;
    const token = localStorage.getItem("token");
    const resultado = document.getElementById("resultado");

    if (!token) {
        resultado.innerHTML = "<span style='color:red;'>Erro: Token de autenticação não encontrado.</span>";
        return;
    }

    try {
        const resp = await fetch(`${API_URL}/buscacep/${cep}`, {
            headers: { "Authorization": `Bearer ${token}` }
        });

        if (resp.ok) {
            const data = await resp.json();
            // Formata a saída para melhor visualização
            resultado.innerHTML = `
                <p><strong>CEP:</strong> ${data.cep || 'N/A'}</p>
                <p><strong>Logradouro:</strong> ${data.logradouro || 'N/A'}</p>
                <p><strong>Bairro:</strong> ${data.bairro || 'N/A'}</p>
                <p><strong>Cidade:</strong> ${data.localidade || 'N/A'}</p>
                <p><strong>UF:</strong> ${data.uf || 'N/A'}</p>
            `;
        } else {
            resultado.innerHTML = `<span style='color:red;'>CEP não encontrado ou erro na busca.</span>`;
        }
    } catch (error) {
        console.error("Erro ao buscar endereço:", error);
        resultado.innerHTML = "<span style='color:red;'>Falha na comunicação com o servidor.</span>";
    }
}