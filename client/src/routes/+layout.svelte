<script>
	import '../app.css';
	let { children } = $props();
</script>

<main class="min-h-screen h-full bg-stone-950 text-white flex flex-col">
	<header id="nav" class="flex justify-between items-center p-4 border-b border-stone-800">
		<div>
			<h1 class="text-xl font-bold text-orange-400">Pizza App</h1>
			<p class="italic tracking-tighter text-sm">We make it fast, we make it easy, and we make it delicious!</p>  
		</div>
	</header>
	<section id="content" class="flex-1">
		{@render children()}
	</section>
	<section id="footer">
		<p class="text-center text-xs italic p-2">
			Copyright &copy; {new Date().getFullYear()} Idriz Pelaj @ <a class="text-orange-400" href="https://github.com/idrizp/feeder" target="_blank">GitHub</a>
		</p>
	</section>
</main>
