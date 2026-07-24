const state = { rooms: [], editingId: null };
const $ = (id) => document.getElementById(id);
const dialog = $('roomDialog');

async function loadRooms() {
  const query = $('search').value.trim();
  const response = await fetch(`/api/rooms${query ? `?search=${encodeURIComponent(query)}` : ''}`);
  state.rooms = await response.json();
  render();
}

function render() {
  const filter = $('statusFilter').value;
  const rooms = state.rooms.filter(room => !filter || room.status === filter);
  $('roomCount').textContent = rooms.length;
  $('roomGrid').innerHTML = rooms.length ? rooms.map(room => `
    <article class="course-card">
      <div class="card-top">
        <span class="code">${room.roomNumber} - ${room.roomType}</span>
        <span class="badge ${room.status}">${labelStatus(room.status)}</span>
      </div>
      <h3>Phòng ${escapeHtml(room.roomNumber)}</h3>
      <p>${escapeHtml(room.description || 'Chưa có thông tin mô tả chi tiết cho phòng này.')}</p>
      <div class="card-footer">
        <span style="font-weight: 700; color: #38bdf8;">${formatCurrency(room.pricePerNight)} / đêm</span>
        <span class="card-actions">
          <button onclick="editRoom(${room.id})">Sửa</button>
          <button onclick="removeRoom(${room.id})">Xóa</button>
        </span>
      </div>
    </article>
  `).join('') : '<div class="empty">Chưa có thông tin phòng nào phù hợp.</div>';
}

function labelStatus(status) { 
  return { 
    AVAILABLE: '🟢 Phòng trống', 
    OCCUPIED: '🟡 Đang có khách', 
    MAINTENANCE: '🔴 Bảo trì' 
  }[status] || status; 
}

function formatCurrency(amount) {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
}

function escapeHtml(value) { 
  return value ? value.replace(/[&<>'"]/g, character => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', "'":'&#39;', '"':'&quot;' }[character])) : ''; 
}

// When Room Template Selection Changes
$('roomTemplateSelect').addEventListener('change', (e) => {
  const opt = e.target.selectedOptions[0];
  if (opt && opt.value) {
    const baseCode = opt.value;
    const existingCount = state.rooms.filter(r => r.roomNumber.startsWith(baseCode)).length;
    const uniqueNumber = existingCount > 0 ? `${baseCode}_${existingCount + 1}` : baseCode;

    $('roomNumber').value = uniqueNumber;
    $('roomType').value = opt.dataset.type || 'SINGLE';
    $('pricePerNight').value = opt.dataset.price || '500000';
    $('description').value = opt.dataset.desc || '';
  } else {
    $('roomNumber').value = '';
    $('description').value = '';
  }
});

function openCreate() { 
  state.editingId = null; 
  $('dialogTitle').textContent = 'Thêm phòng khách sạn'; 
  $('roomForm').reset(); 
  $('roomNumber').readOnly = true;
  $('description').readOnly = true;
  $('status').value = 'AVAILABLE'; 
  dialog.showModal(); 
}

window.editRoom = (id) => { 
  const room = state.rooms.find(item => item.id === id); 
  state.editingId = id; 
  $('dialogTitle').textContent = 'Chỉnh sửa thông tin phòng'; 
  $('roomId').value = id; 
  $('roomNumber').readOnly = false;
  $('description').readOnly = false;
  $('roomNumber').value = room.roomNumber; 
  $('roomType').value = room.roomType; 
  $('pricePerNight').value = room.pricePerNight; 
  $('description').value = room.description || ''; 
  $('status').value = room.status; 
  dialog.showModal(); 
};

window.removeRoom = async (id) => { 
  if (!confirm('Bạn có chắc chắn muốn xóa thông tin phòng này?')) return; 
  await fetch(`/api/rooms/${id}`, { method: 'DELETE' }); 
  await loadRooms(); 
};

$('roomForm').addEventListener('submit', async (event) => { 
  event.preventDefault(); 
  if (!$('roomNumber').value) {
    alert('Vui lòng chọn hoặc nhập số phòng!');
    return;
  }
  const payload = { 
    roomNumber: $('roomNumber').value, 
    roomType: $('roomType').value, 
    description: $('description').value, 
    pricePerNight: Number($('pricePerNight').value), 
    status: $('status').value 
  }; 
  const url = state.editingId ? `/api/rooms/${state.editingId}` : '/api/rooms'; 
  await fetch(url, { 
    method: state.editingId ? 'PUT' : 'POST', 
    headers: { 'Content-Type': 'application/json' }, 
    body: JSON.stringify(payload) 
  }); 
  dialog.close(); 
  await loadRooms(); 
});

$('addButton').onclick = openCreate; 
$('closeButton').onclick = () => dialog.close(); 
$('cancelButton').onclick = () => dialog.close(); 
$('statusFilter').onchange = render; 
$('search').oninput = loadRooms; 

loadRooms();
