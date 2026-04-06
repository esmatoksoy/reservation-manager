let guestIndex = 0;

function addGuest() {
    const container = document.getElementById('guestsContainer');

    const guestDiv = document.createElement('div');
    guestDiv.className = 'guest-box';

    guestDiv.innerHTML = `
        <div class="guest-header">Guest ${guestIndex + 1} Details</div>

        <div class="form-group">
            <label>Guest Name</label>
            <input name="guests[${guestIndex}].name" type="text" placeholder="Guest Name" required />
        </div>

        <div class="form-group">
            <label>Allergies or Dietary Restrictions</label>
            <div class="checkbox-group">
                <label class="checkbox-item">
                    <input type="checkbox" name="guests[${guestIndex}].allergies" value="Gluten-Free"> Gluten-Free
                </label>
                <label class="checkbox-item">
                    <input type="checkbox" name="guests[${guestIndex}].allergies" value="Dairy-Free"> Dairy-Free
                </label>
                <label class="checkbox-item">
                    <input type="checkbox" name="guests[${guestIndex}].allergies" value="Peanuts"> Peanuts
                </label>
                <label class="checkbox-item">
                    <input type="checkbox" name="guests[${guestIndex}].allergies" value="Vegetarian"> Vegetarian
                </label>
            </div>
            <label class="margin-top10">
                <input type="checkbox" onchange="toggleOtherInput(this)"> Other (please specify)
            </label>
            <input type="text" name="guests[${guestIndex}].allergiesOther" placeholder="Specify allergy..." style="display:none;">
        </div>

        <div class="form-group">
            <label>Extra Needs</label>
            <textarea name="guests[${guestIndex}].extraNeeds" rows="2" placeholder="e.g., extra blanket"></textarea>
        </div>
    `;

    container.appendChild(guestDiv);
    guestIndex++;
}

function toggleOtherInput(checkboxEl) {
    const otherInput = checkboxEl.parentElement.nextElementSibling;
    if (otherInput) {
        otherInput.style.display = checkboxEl.checked ? 'block' : 'none';
        if (!checkboxEl.checked) otherInput.value = '';
    }
}
