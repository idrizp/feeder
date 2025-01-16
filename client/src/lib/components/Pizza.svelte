<script>
    export let name = "Pepperoni";
    export let toppings = [
      "Pepperoni",
      "Sausage",
      "Mushrooms",
      "Pineapple",
      "Olives",
      "Onions",
      "Tomatoes"
    ];
    export let allergens = [
      "Eggs",
      "Milk",
      "Soy",
      "Wheat",
      "Fish",
      "Shellfish",
      "Peanuts"
    ];
    export let price = 10;
  
    let size = "small";
    let priceMap = { small: 10, medium: 12, large: 14 };
  
    const handleSizeChange = (event) => {
      size = event.target.value;
      price = priceMap[size];
    };
  </script>
  
  <div class="p-4 flex flex-col gap-y-5 border-stone-900 border rounded-lg lg:px-12">
    <p class="text-center text-3xl">{name}</p>
  
    <div class="flex flex-row gap-x-2 justify-center bg-stone-900 p-2 rounded-lg">
      <div>
        <input
          type="radio"
          id="small-{name}"
          name="size"
          value="small"
          bind:group={size}
          on:change={handleSizeChange}
          checked={size === "small"}
        />
        <label for="small">Small</label>
      </div>
      <div>
        <input
          type="radio"
          id="medium-{name}"
          name="size"
          value="medium"
          bind:group={size}
          on:change={handleSizeChange}
          checked={size === "medium"}
        />
        <label for="medium">Medium</label>
      </div>
      <div>
        <input
          type="radio"
          id="large-{name}"
          name="size"
          value="large"
          bind:group={size}
          on:change={handleSizeChange}
          checked={size === "large"}
        />
        <label for="large">Large</label>
      </div>
    </div>
  
    <img
      src="https://images.pexels.com/photos/708587/pexels-photo-708587.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
      alt="{name} Pizza"
      class="aspect-square w-64 self-center rounded-full border-2 border-orange-400 shadow-lg"
    />
    
    <a
      class="bg-orange-600 text-white rounded-full px-4 py-2 tracking-tighter"
      id="order-{name}"
      href="/pizza"
    >
      Order Now For ${price}
</a>
  
    <div class="flex flex-row justify-between lg:justify-normal gap-x-6">
      <div id="toppings-{name}">
        <p class="italic underline">Toppings:</p>
        <ul class="list-disc list-inside px-2">
          {#each toppings as topping}
            <li>{topping}</li>
          {/each}
        </ul>
      </div>
      
      <div id="allergens-{name}">
        <p class="italic underline">Allergens:</p>
        <ul class="list-disc list-inside px-2 text-right lg:text-left">
          {#each allergens as allergen}
            <li>{allergen}</li>
          {/each}
        </ul>    
      </div>
    </div>
  </div>
  