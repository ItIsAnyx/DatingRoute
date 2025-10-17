import json

class JSONConverter:
    def __init__(self, text) -> None:
        self.text = text

    def convert_to_json(self):
        try:
            data = json.loads(self.text)
            if isinstance(data, dict):
                return data
            else:
                print("JSON warning: data is not dict")
                return {"title": "Untitled", "message": str(data)}
        except json.JSONDecodeError:
            print("JSON exception")
            return {"title": "Auto title", "message": self.text}

    def __repr__(self):
        return json.dumps(self.convert_to_json(), ensure_ascii=False, indent=2)

x = JSONConverter("""{\n  "title": "Топ-5 книг в жанре научной фантастики",\n  "message": "1. «Солнечный свет» Джона Кэмертона\n2. «Атмосфера» Альберта Эйнштейна\n3. «Звездные войны» Фрэнсиса Питера\n4. «Человек-паук: Возвращение домой» Бенджамина Дарта\n5. «Джонни и Маленький» Томаса Шермана"\n}""")
y = JSONConverter("{'title': 'Список самых популярных пирожков', 'message': 'Популярными считаются булочки с коржом и начинкой из молочного или мясного цюрихского соуса, а также тортчики и сладкие блинчики.'")
print(x.convert_to_json())
print(y.convert_to_json())