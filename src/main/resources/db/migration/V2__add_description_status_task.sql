ALTER table public.task Add column if not exists description text;
ALTER table public.task Add column if not exists status text;
ALTER table public.task drop column if exists checkbox;
