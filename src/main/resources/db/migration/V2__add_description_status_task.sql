ALTER table public.tasks Add column if not exists description text;
ALTER table public.tasks Add column if not exists status text;
ALTER table public.tasks drop column if exists checkbox;
