-- +goose Up
-- +goose StatementBegin
CREATE TABLE [dbo].[Episode] (
    [Id] UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
    [Title] NVARCHAR(100) NOT NULL,
    [Number] VARCHAR(10) NOT NULL,
    [Description] NVARCHAR(1000),
    [Url] VARCHAR(50),
    [ReleaseDate] DATE,
    [CreatedTime] DATETIME,
    [UpdatedTime] DATETIME,
    [Duration] INT NOT NULL,
    [View] INT NOT NULL DEFAULT 0,
    [OpeningStart] INT,
    [OpeningEnd] INT,
    [IsDeleted] BIT NOT NULL DEFAULT 0,
    [CommentSection] UNIQUEIDENTIFIER UNIQUE FOREIGN KEY REFERENCES [dbo].[CommentSection]([Id]),
    [Season] UNIQUEIDENTIFIER NOT NULL FOREIGN KEY REFERENCES [dbo].[Season]([Id])
);
-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin
DROP TABLE [dbo].[Episode];
-- +goose StatementEnd
